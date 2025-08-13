package online.bingzi.onebot.action

import com.google.gson.JsonObject
import online.bingzi.onebot.client.OneBotWebSocketClient
import online.bingzi.onebot.config.OneBotConfig
import taboolib.common.platform.function.console
import taboolib.module.lang.sendWarn
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

/**
 * 动作工厂，用于发送 OneBot API 请求
 */
class ActionFactory(private var client: OneBotWebSocketClient? = null) {

    private val pendingActions = ConcurrentHashMap<String, CompletableFuture<JsonObject>>()

    /**
     * 设置客户端（用于解决循环依赖）
     */
    fun setClient(client: OneBotWebSocketClient) {
        this.client = client
    }

    /**
     * 发送动作并等待响应
     */
    fun sendAction(action: String, params: JsonObject? = null): CompletableFuture<JsonObject> {
        val currentClient = client ?: run {
            val future = CompletableFuture<JsonObject>()
            future.completeExceptionally(RuntimeException("客户端未初始化"))
            return future
        }

        val echo = UUID.randomUUID().toString()
        val future = CompletableFuture<JsonObject>()

        pendingActions[echo] = future

        // 设置超时 (Java 8 兼容方式)
        CompletableFuture<JsonObject>()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!future.isDone) {
                    future.completeExceptionally(RuntimeException("请求超时"))
                    pendingActions.remove(echo)
                }
            }
        }, 10000) // 10秒超时

        future.whenComplete { _, _ ->
            timer.cancel()
            pendingActions.remove(echo)
        }

        if (currentClient.sendAction(action, params, echo)) {
            if (OneBotConfig.debugEnabled) {
                console().sendWarn("actionSent", action, echo)
            }
        } else {
            future.completeExceptionally(RuntimeException("发送动作失败"))
        }

        return future
    }

    /**
     * 处理响应
     */
    fun handleResponse(json: JsonObject) {
        val echo = json.get("echo")?.asString ?: return
        val future = pendingActions.remove(echo) ?: return

        if (OneBotConfig.debugEnabled) {
            console().sendWarn("responseReceived", echo)
        }

        val retCode = json.get("retcode")?.asInt ?: -1
        if (retCode == 0) {
            future.complete(json)
        } else {
            val msg = json.get("msg")?.asString ?: "未知错误"
            future.completeExceptionally(RuntimeException("API 调用失败: $msg (retcode: $retCode)"))
        }
    }

    // API 方法封装

    /**
     * 发送私聊消息
     */
    fun sendPrivateMsg(userId: Long, message: String, autoEscape: Boolean = false): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("user_id", userId)
            addProperty("message", message)
            addProperty("auto_escape", autoEscape)
        }
        return sendAction("send_private_msg", params)
    }

    /**
     * 发送群消息
     */
    fun sendGroupMsg(groupId: Long, message: String, autoEscape: Boolean = false): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("group_id", groupId)
            addProperty("message", message)
            addProperty("auto_escape", autoEscape)
        }
        return sendAction("send_group_msg", params)
    }

    /**
     * 撤回消息
     */
    fun deleteMsg(messageId: Int): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("message_id", messageId)
        }
        return sendAction("delete_msg", params)
    }

    /**
     * 获取消息
     */
    fun getMsg(messageId: Int): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("message_id", messageId)
        }
        return sendAction("get_msg", params)
    }

    /**
     * 群组单人禁言
     */
    fun setGroupBan(groupId: Long, userId: Long, duration: Int): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("group_id", groupId)
            addProperty("user_id", userId)
            addProperty("duration", duration)
        }
        return sendAction("set_group_ban", params)
    }

    /**
     * 群组踢人
     */
    fun setGroupKick(groupId: Long, userId: Long, rejectAddRequest: Boolean = false): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("group_id", groupId)
            addProperty("user_id", userId)
            addProperty("reject_add_request", rejectAddRequest)
        }
        return sendAction("set_group_kick", params)
    }

    /**
     * 处理加好友请求
     */
    fun setFriendAddRequest(flag: String, approve: Boolean, remark: String = ""): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("flag", flag)
            addProperty("approve", approve)
            if (remark.isNotEmpty()) {
                addProperty("remark", remark)
            }
        }
        return sendAction("set_friend_add_request", params)
    }

    /**
     * 处理加群请求／邀请
     */
    fun setGroupAddRequest(
        flag: String,
        subType: String,
        approve: Boolean,
        reason: String = ""
    ): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("flag", flag)
            addProperty("sub_type", subType)
            addProperty("approve", approve)
            if (reason.isNotEmpty()) {
                addProperty("reason", reason)
            }
        }
        return sendAction("set_group_add_request", params)
    }

    /**
     * 获取登录号信息
     */
    fun getLoginInfo(): CompletableFuture<JsonObject> {
        return sendAction("get_login_info")
    }

    /**
     * 获取好友列表
     */
    fun getFriendList(): CompletableFuture<JsonObject> {
        return sendAction("get_friend_list")
    }

    /**
     * 获取群列表
     */
    fun getGroupList(): CompletableFuture<JsonObject> {
        return sendAction("get_group_list")
    }

    /**
     * 获取群成员列表
     */
    fun getGroupMemberList(groupId: Long): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("group_id", groupId)
        }
        return sendAction("get_group_member_list", params)
    }

    /**
     * 获取群成员信息
     */
    fun getGroupMemberInfo(groupId: Long, userId: Long, noCache: Boolean = false): CompletableFuture<JsonObject> {
        val params = JsonObject().apply {
            addProperty("group_id", groupId)
            addProperty("user_id", userId)
            addProperty("no_cache", noCache)
        }
        return sendAction("get_group_member_info", params)
    }
}