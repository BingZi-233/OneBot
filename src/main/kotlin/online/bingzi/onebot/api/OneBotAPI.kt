package online.bingzi.onebot.api

import online.bingzi.onebot.action.ActionFactory
import online.bingzi.onebot.client.OneBotWebSocketClient
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendError
import taboolib.module.lang.sendInfo

/**
 * OneBot API 接口
 * 提供给其他插件使用的公共接口
 */
object OneBotAPI {

    private var actionFactory: ActionFactory? = null
    private var client: OneBotWebSocketClient? = null

    /**
     * 初始化 API
     */
    internal fun initialize(actionFactory: ActionFactory, client: OneBotWebSocketClient) {
        this.actionFactory = actionFactory
        this.client = client
        console().sendInfo("apiInitialized")
    }

    /**
     * 检查是否已连接
     */
    fun isConnected(): Boolean {
        return client?.isOpen == true
    }

    /**
     * 发送私聊消息
     * @param userId 用户QQ号
     * @param message 消息内容
     * @param autoEscape 是否作为纯文本发送
     * @param callback 回调函数，参数为是否发送成功
     */
    fun sendPrivateMessage(userId: Long, message: String, autoEscape: Boolean = false, callback: (Boolean) -> Unit = {}) {
        actionFactory?.sendPrivateMsg(userId, message, autoEscape)?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("sendPrivateMessageFailed", throwable.message ?: "Unknown error")
                    callback(false)
                } else {
                    callback(result != null)
                }
            }
        }
    }

    /**
     * 发送群消息
     * @param groupId 群号
     * @param message 消息内容
     * @param autoEscape 是否作为纯文本发送
     * @param callback 回调函数，参数为是否发送成功
     */
    fun sendGroupMessage(groupId: Long, message: String, autoEscape: Boolean = false, callback: (Boolean) -> Unit = {}) {
        actionFactory?.sendGroupMsg(groupId, message, autoEscape)?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("sendGroupMessageFailed", throwable.message ?: "Unknown error")
                    callback(false)
                } else {
                    callback(result != null)
                }
            }
        }
    }

    /**
     * 撤回消息
     * @param messageId 消息ID
     * @param callback 回调函数，参数为是否撤回成功
     */
    fun deleteMessage(messageId: Int, callback: (Boolean) -> Unit = {}) {
        actionFactory?.deleteMsg(messageId)?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("deleteMessageFailed", throwable.message ?: "Unknown error")
                    callback(false)
                } else {
                    callback(result != null)
                }
            }
        }
    }

    /**
     * 禁言群成员
     * @param groupId 群号
     * @param userId 用户QQ号
     * @param duration 禁言时长（秒）
     * @param callback 回调函数，参数为是否成功
     */
    fun banGroupMember(groupId: Long, userId: Long, duration: Int, callback: (Boolean) -> Unit = {}) {
        actionFactory?.setGroupBan(groupId, userId, duration)?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("banGroupMemberFailed", throwable.message ?: "Unknown error")
                    callback(false)
                } else {
                    callback(result != null)
                }
            }
        }
    }

    /**
     * 踢出群成员
     * @param groupId 群号
     * @param userId 用户QQ号
     * @param rejectAddRequest 是否拒绝此人的加群请求
     * @param callback 回调函数，参数为是否成功
     */
    fun kickGroupMember(groupId: Long, userId: Long, rejectAddRequest: Boolean = false, callback: (Boolean) -> Unit = {}) {
        actionFactory?.setGroupKick(groupId, userId, rejectAddRequest)?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("kickGroupMemberFailed", throwable.message ?: "Unknown error")
                    callback(false)
                } else {
                    callback(result != null)
                }
            }
        }
    }

    /**
     * 获取好友列表
     * @param callback 回调函数，参数为好友列表的JSON数据，失败返回null
     */
    fun getFriendList(callback: (String?) -> Unit) {
        actionFactory?.getFriendList()?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("getFriendListFailed", throwable.message ?: "Unknown error")
                    callback(null)
                } else {
                    callback(result?.toString())
                }
            }
        }
    }

    /**
     * 获取群列表
     * @param callback 回调函数，参数为群列表的JSON数据，失败返回null
     */
    fun getGroupList(callback: (String?) -> Unit) {
        actionFactory?.getGroupList()?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("getGroupListFailed", throwable.message ?: "Unknown error")
                    callback(null)
                } else {
                    callback(result?.toString())
                }
            }
        }
    }

    /**
     * 获取群成员列表
     * @param groupId 群号
     * @param callback 回调函数，参数为群成员列表的JSON数据，失败返回null
     */
    fun getGroupMemberList(groupId: Long, callback: (String?) -> Unit) {
        actionFactory?.getGroupMemberList(groupId)?.handle { result, throwable ->
            submit(async = false) {
                if (throwable != null) {
                    console().sendError("getGroupMemberListFailed", throwable.message ?: "Unknown error")
                    callback(null)
                } else {
                    callback(result?.toString())
                }
            }
        }
    }
}