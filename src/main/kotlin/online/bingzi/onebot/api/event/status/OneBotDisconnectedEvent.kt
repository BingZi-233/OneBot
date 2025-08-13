package online.bingzi.onebot.api.event.status

/**
 * OneBot连接断开事件
 * 
 * 当OneBot WebSocket连接断开时触发此事件
 * 其他插件可以监听此事件来处理连接断开的逻辑
 */
class OneBotDisconnectedEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    val serverUrl: String,
    reason: String,
    val isNormalClose: Boolean = false,
    val closeCode: Int = -1
) : StatusEvent(time, selfId, rawData, "disconnected", reason)