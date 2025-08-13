package online.bingzi.onebot.event.message

import online.bingzi.onebot.event.base.OneBotEvent

/**
 * 消息事件基类
 */
abstract class MessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val rawData: String,
    val messageType: String,
    val subType: String,
    val messageId: Int,
    val userId: Long,
    val message: String,
    val font: Int
) : OneBotEvent() {

    override val postType: String = "message"

    /**
     * 回复消息
     */
    abstract fun reply(message: String, autoEscape: Boolean = false)

    /**
     * 回复消息（带引用）
     */
    abstract fun replyWithQuote(message: String, autoEscape: Boolean = false)
}

