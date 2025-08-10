package online.bingzi.onebot.event.message

import online.bingzi.onebot.api.OneBotAPI
import online.bingzi.onebot.event.base.OneBotEvent

/**
 * 消息事件基类
 */
abstract class MessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val rawData: String,
    val messageType: String,
    val subType: String,
    val messageId: Int,
    val userId: Long,
    val message: String,
    val font: Int
) : OneBotEvent() {

    override val postType: String = "message"

    /**
     * 回复消息
     */
    abstract fun reply(message: String, autoEscape: Boolean = false)

    /**
     * 回复消息（带引用）
     */
    abstract fun replyWithQuote(message: String, autoEscape: Boolean = false)
}

/**
 * 私聊消息事件
 */
class PrivateMessageEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    subType: String,
    messageId: Int,
    userId: Long,
    message: String,
    font: Int
) : MessageEvent(time, selfId, rawData, "private", subType, messageId, userId, message, font) {

    override fun reply(message: String, autoEscape: Boolean) {
        OneBotAPI.sendPrivateMessage(userId, message, autoEscape)
    }

    override fun replyWithQuote(message: String, autoEscape: Boolean) {
        val quotedMessage = "[CQ:reply,id=$messageId]$message"
        OneBotAPI.sendPrivateMessage(userId, quotedMessage, autoEscape)
    }
}

/**
 * 群消息事件
 */
class GroupMessageEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    subType: String,
    messageId: Int,
    userId: Long,
    message: String,
    font: Int,
    val groupId: Long,
    val anonymous: Any? = null
) : MessageEvent(time, selfId, rawData, "group", subType, messageId, userId, message, font) {

    override fun reply(message: String, autoEscape: Boolean) {
        OneBotAPI.sendGroupMessage(groupId, message, autoEscape)
    }

    override fun replyWithQuote(message: String, autoEscape: Boolean) {
        val quotedMessage = "[CQ:reply,id=$messageId]$message"
        OneBotAPI.sendGroupMessage(groupId, quotedMessage, autoEscape)
    }

    /**
     * @某人回复
     */
    fun replyWithAt(message: String, autoEscape: Boolean = false) {
        val atMessage = "[CQ:at,qq=$userId] $message"
        OneBotAPI.sendGroupMessage(groupId, atMessage, autoEscape)
    }
}