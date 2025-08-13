package online.bingzi.onebot.api.event.request

/**
 * 加好友请求事件
 * 
 * 当有人申请添加机器人为好友时触发此事件
 * 其他插件可以监听此事件来处理好友申请
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