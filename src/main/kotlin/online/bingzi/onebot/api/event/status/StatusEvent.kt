package online.bingzi.onebot.api.event.status

import online.bingzi.onebot.api.event.OneBotEvent

/**
 * 状态事件基类
 * 
 * 用于表示OneBot连接状态变化的事件
 * 其他插件可以监听此事件来处理OneBot的状态变化
 */
abstract class StatusEvent(
    override val time: Long,
    override val selfId: Long,
    override val rawData: String,
    val statusType: String,
    val reason: String = ""
) : OneBotEvent() {

    override val postType: String = "status"
}