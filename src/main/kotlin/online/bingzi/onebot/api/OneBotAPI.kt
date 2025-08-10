package online.bingzi.onebot.api

import online.bingzi.onebot.action.ActionFactory
import online.bingzi.onebot.client.OneBotWebSocketClient
import taboolib.common.platform.function.console

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
        console().sendMessage("§a[OneBot] API 已初始化")
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
     * @return 是否发送成功
     */
    fun sendPrivateMessage(userId: Long, message: String, autoEscape: Boolean = false): Boolean {
        return try {
            actionFactory?.sendPrivateMsg(userId, message, autoEscape)?.get() != null
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 发送私聊消息失败: ${e.message}")
            false
        }
    }

    /**
     * 发送群消息
     * @param groupId 群号
     * @param message 消息内容
     * @param autoEscape 是否作为纯文本发送
     * @return 是否发送成功
     */
    fun sendGroupMessage(groupId: Long, message: String, autoEscape: Boolean = false): Boolean {
        return try {
            actionFactory?.sendGroupMsg(groupId, message, autoEscape)?.get() != null
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 发送群消息失败: ${e.message}")
            false
        }
    }

    /**
     * 撤回消息
     * @param messageId 消息ID
     * @return 是否撤回成功
     */
    fun deleteMessage(messageId: Int): Boolean {
        return try {
            actionFactory?.deleteMsg(messageId)?.get() != null
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 撤回消息失败: ${e.message}")
            false
        }
    }

    /**
     * 禁言群成员
     * @param groupId 群号
     * @param userId 用户QQ号
     * @param duration 禁言时长（秒）
     * @return 是否成功
     */
    fun banGroupMember(groupId: Long, userId: Long, duration: Int): Boolean {
        return try {
            actionFactory?.setGroupBan(groupId, userId, duration)?.get() != null
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 禁言群成员失败: ${e.message}")
            false
        }
    }

    /**
     * 踢出群成员
     * @param groupId 群号
     * @param userId 用户QQ号
     * @param rejectAddRequest 是否拒绝此人的加群请求
     * @return 是否成功
     */
    fun kickGroupMember(groupId: Long, userId: Long, rejectAddRequest: Boolean = false): Boolean {
        return try {
            actionFactory?.setGroupKick(groupId, userId, rejectAddRequest)?.get() != null
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 踢出群成员失败: ${e.message}")
            false
        }
    }

    /**
     * 获取好友列表
     * @return 好友列表的JSON数据，失败返回null
     */
    fun getFriendList(): String? {
        return try {
            actionFactory?.getFriendList()?.get()?.toString()
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 获取好友列表失败: ${e.message}")
            null
        }
    }

    /**
     * 获取群列表
     * @return 群列表的JSON数据，失败返回null
     */
    fun getGroupList(): String? {
        return try {
            actionFactory?.getGroupList()?.get()?.toString()
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 获取群列表失败: ${e.message}")
            null
        }
    }

    /**
     * 获取群成员列表
     * @param groupId 群号
     * @return 群成员列表的JSON数据，失败返回null
     */
    fun getGroupMemberList(groupId: Long): String? {
        return try {
            actionFactory?.getGroupMemberList(groupId)?.get()?.toString()
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 获取群成员列表失败: ${e.message}")
            null
        }
    }
}