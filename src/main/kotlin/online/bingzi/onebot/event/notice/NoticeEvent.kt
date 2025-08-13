package online.bingzi.onebot.event.notice

import online.bingzi.onebot.event.base.OneBotEvent

/**
 * 通知事件基类
 */
abstract class NoticeEvent(
    override val time: Long,
    override val selfId: Long,
    override val rawData: String,
    val noticeType: String
) : OneBotEvent() {

    override val postType: String = "notice"
}

