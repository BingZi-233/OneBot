package online.bingzi.onebot.event.message

import online.bingzi.onebot.api.OneBotAPI

/**
 * 群消息事件
 */
class GroupMessageEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    subType: String,
    messageId: Int,
    userId: Long,
    message: String,
    font: Int,
    val groupId: Long,
    val anonymous: Any? = null
) : MessageEvent(time, selfId, rawData, "group", subType, messageId, userId, message, font) {

    override fun reply(message: String, autoEscape: Boolean) {
        OneBotAPI.sendGroupMessage(groupId, message, autoEscape)
    }

    override fun replyWithQuote(message: String, autoEscape: Boolean) {
        val quotedMessage = "[CQ:reply,id=$messageId]$message"
        OneBotAPI.sendGroupMessage(groupId, quotedMessage, autoEscape)
    }

    /**
     * @某人回复
     */
    fun replyWithAt(message: String, autoEscape: Boolean = false) {
        val atMessage = "[CQ:at,qq=$userId] $message"
        OneBotAPI.sendGroupMessage(groupId, atMessage, autoEscape)
    }
}