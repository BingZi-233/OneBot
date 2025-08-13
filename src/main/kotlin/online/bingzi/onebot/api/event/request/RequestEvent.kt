package online.bingzi.onebot.api.event.request

import online.bingzi.onebot.api.event.OneBotEvent

/**
 * 请求事件基类
 * 
 * 其他插件可以监听此事件来处理OneBot的各种请求
 */
abstract class RequestEvent(
    override val time: Long,
    override val selfId: Long,
    override val rawData: String,
    val requestType: String
) : OneBotEvent() {

    override val postType: String = "request"
}