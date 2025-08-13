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

