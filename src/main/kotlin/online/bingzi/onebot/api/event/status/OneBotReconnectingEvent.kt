package online.bingzi.onebot.api.event.status

/**
 * OneBot重连事件
 * 
 * 当OneBot开始尝试重连时触发此事件
 * 其他插件可以监听此事件来处理重连逻辑
 */
class OneBotReconnectingEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    val serverUrl: String,
    val attemptCount: Int,
    val maxAttempts: Int,
    val retryDelay: Long
) : StatusEvent(time, selfId, rawData, "reconnecting", "Attempting to reconnect (${attemptCount}/${maxAttempts})")