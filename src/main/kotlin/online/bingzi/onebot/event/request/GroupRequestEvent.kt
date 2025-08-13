package online.bingzi.onebot.event.request

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