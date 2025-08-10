package online.bingzi.onebot.event.request

import online.bingzi.onebot.event.base.OneBotEvent

/**
 * 请求事件基类
 */
abstract class RequestEvent(
    override val time: Long,
    override val selfId: Long,
    override val rawData: String,
    val requestType: String
) : OneBotEvent() {

    override val postType: String = "request"
}

/**
 * 加好友请求事件
 */
class FriendRequestEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    val userId: Long,
    val comment: String,
    val flag: String
) : RequestEvent(time, selfId, rawData, "friend") {

    /**
     * 同意好友请求
     */
    fun approve(remark: String = ""): Boolean {
        // 需要通过ActionFactory处理，这里先返回false
        // TODO: 实现好友请求处理
        return false
    }

    /**
     * 拒绝好友请求
     */
    fun reject(): Boolean {
        // 需要通过ActionFactory处理
        // TODO: 实现好友请求处理
        return false
    }
}

/**
 * 加群请求事件
 */
class GroupRequestEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    val subType: String,
    val groupId: Long,
    val userId: Long,
    val comment: String,
    val flag: String
) : RequestEvent(time, selfId, rawData, "group") {

    /**
     * 同意请求
     */
    fun approve(): Boolean {
        // 需要通过ActionFactory处理
        // TODO: 实现群请求处理
        return false
    }

    /**
     * 拒绝请求
     */
    fun reject(reason: String = ""): Boolean {
        // 需要通过ActionFactory处理
        // TODO: 实现群请求处理
        return false
    }
}