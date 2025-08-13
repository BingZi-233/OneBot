package online.bingzi.onebot.api.event.notice

import online.bingzi.onebot.api.event.OneBotEvent

/**
 * 通知事件基类
 * 
 * 其他插件可以监听此事件来处理OneBot的各种通知
 */
abstract class NoticeEvent(
    override val time: Long,
    override val selfId: Long,
    override val rawData: String,
    val noticeType: String
) : OneBotEvent() {

    override val postType: String = "notice"
}