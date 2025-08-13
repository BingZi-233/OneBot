package online.bingzi.onebot.api.event.status

/**
 * OneBot连接成功事件
 * 
 * 当OneBot WebSocket连接成功建立时触发此事件
 * 其他插件可以监听此事件来处理连接成功后的逻辑
 */
class OneBotConnectedEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    val serverUrl: String,
    val reconnectCount: Int = 0
) : StatusEvent(time, selfId, rawData, "connected", "WebSocket connection established")