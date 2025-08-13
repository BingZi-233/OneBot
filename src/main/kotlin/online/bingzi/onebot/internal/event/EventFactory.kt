package online.bingzi.onebot.internal.event

import com.google.gson.JsonObject
import online.bingzi.onebot.api.event.OneBotEvent
import online.bingzi.onebot.api.event.message.GroupMessageEvent
import online.bingzi.onebot.api.event.message.PrivateMessageEvent
import online.bingzi.onebot.api.event.notice.FriendAddNotice
import online.bingzi.onebot.api.event.notice.GroupBanNotice
import online.bingzi.onebot.api.event.notice.GroupDecreaseNotice
import online.bingzi.onebot.api.event.notice.GroupIncreaseNotice
import online.bingzi.onebot.api.event.request.FriendRequestEvent
import online.bingzi.onebot.api.event.request.GroupRequestEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendError
import taboolib.module.lang.sendWarn

/**
 * 事件工厂，用于将 OneBot 消息转换为对应的事件
 * 
 * 内部事件处理组件，负责解析OneBot协议消息并创建对应的API事件
 */
class EventFactory {

    /**
     * 处理收到的消息
     */
    fun handleMessage(jsonObject: JsonObject) {
        try {
            val postType = jsonObject.get("post_type")?.asString ?: return
            val event = createEvent(jsonObject, postType)

            if (event != null) {
                // 在异步线程中调用事件，避免阻塞WebSocket消息接收线程
                submit(async = true) {
                    try {
                        event.call()
                    } catch (e: Exception) {
                        console().sendError("eventProcessingError", e.message ?: "Unknown error")
                        if (jsonObject.has("post_type")) {
                            console().sendWarn("eventTypeDebug", jsonObject.get("post_type").asString)
                        }
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            console().sendError("eventCreationError", e.message ?: "Unknown error")
            e.printStackTrace()
        }
    }

    /**
     * 根据消息类型创建对应的事件
     */
    private fun createEvent(json: JsonObject, postType: String): OneBotEvent? {
        val time = json.get("time")?.asLong ?: System.currentTimeMillis() / 1000
        val selfId = json.get("self_id")?.asLong ?: 0L
        val rawData = json.toString()

        return when (postType) {
            "message" -> createMessageEvent(json, time, selfId, rawData)
            "notice" -> createNoticeEvent(json, time, selfId, rawData)
            "request" -> createRequestEvent(json, time, selfId, rawData)
            "meta_event" -> null // 元事件暂时忽略
            else -> {
                null
            }
        }
    }

    /**
     * 创建消息事件
     */
    private fun createMessageEvent(json: JsonObject, time: Long, selfId: Long, rawData: String): OneBotEvent? {
        val messageType = json.get("message_type")?.asString ?: return null
        val subType = json.get("sub_type")?.asString ?: ""
        val messageId = json.get("message_id")?.asInt ?: 0
        val userId = json.get("user_id")?.asLong ?: 0L

        // 处理 message 字段，可能是字符串或数组
        val messageElement = json.get("message")
        val message = when {
            messageElement == null -> ""
            messageElement.isJsonPrimitive -> messageElement.asString
            messageElement.isJsonArray -> messageElement.toString() // 将消息段数组转换为JSON字符串
            else -> messageElement.toString()
        }

        val font = json.get("font")?.asInt ?: 0

        return when (messageType) {
            "private" -> PrivateMessageEvent(
                time, selfId, rawData, subType, messageId, userId, message, font
            )

            "group" -> {
                val groupId = json.get("group_id")?.asLong ?: 0L
                val anonymous = json.get("anonymous")
                GroupMessageEvent(
                    time, selfId, rawData, subType, messageId, userId, message, font, groupId, anonymous
                )
            }

            else -> null
        }
    }

    /**
     * 创建通知事件
     */
    private fun createNoticeEvent(json: JsonObject, time: Long, selfId: Long, rawData: String): OneBotEvent? {
        val noticeType = json.get("notice_type")?.asString ?: return null

        return when (noticeType) {
            "group_increase" -> {
                val subType = json.get("sub_type")?.asString ?: ""
                val groupId = json.get("group_id")?.asLong ?: 0L
                val operatorId = json.get("operator_id")?.asLong ?: 0L
                val userId = json.get("user_id")?.asLong ?: 0L
                GroupIncreaseNotice(time, selfId, rawData, subType, groupId, operatorId, userId)
            }

            "group_decrease" -> {
                val subType = json.get("sub_type")?.asString ?: ""
                val groupId = json.get("group_id")?.asLong ?: 0L
                val operatorId = json.get("operator_id")?.asLong ?: 0L
                val userId = json.get("user_id")?.asLong ?: 0L
                GroupDecreaseNotice(time, selfId, rawData, subType, groupId, operatorId, userId)
            }

            "group_ban" -> {
                val subType = json.get("sub_type")?.asString ?: ""
                val groupId = json.get("group_id")?.asLong ?: 0L
                val operatorId = json.get("operator_id")?.asLong ?: 0L
                val userId = json.get("user_id")?.asLong ?: 0L
                val duration = json.get("duration")?.asInt ?: 0
                GroupBanNotice(time, selfId, rawData, subType, groupId, operatorId, userId, duration)
            }

            "friend_add" -> {
                val userId = json.get("user_id")?.asLong ?: 0L
                FriendAddNotice(time, selfId, rawData, userId)
            }

            else -> {
                null
            }
        }
    }

    /**
     * 创建请求事件
     */
    private fun createRequestEvent(json: JsonObject, time: Long, selfId: Long, rawData: String): OneBotEvent? {
        val requestType = json.get("request_type")?.asString ?: return null

        return when (requestType) {
            "friend" -> {
                val userId = json.get("user_id")?.asLong ?: 0L
                val comment = json.get("comment")?.asString ?: ""
                val flag = json.get("flag")?.asString ?: ""
                FriendRequestEvent(time, selfId, rawData, userId, comment, flag)
            }

            "group" -> {
                val subType = json.get("sub_type")?.asString ?: ""
                val groupId = json.get("group_id")?.asLong ?: 0L
                val userId = json.get("user_id")?.asLong ?: 0L
                val comment = json.get("comment")?.asString ?: ""
                val flag = json.get("flag")?.asString ?: ""
                GroupRequestEvent(time, selfId, rawData, subType, groupId, userId, comment, flag)
            }

            else -> {
                null
            }
        }
    }
}