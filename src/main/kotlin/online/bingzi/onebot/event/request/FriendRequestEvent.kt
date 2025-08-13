package online.bingzi.onebot.event.request

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