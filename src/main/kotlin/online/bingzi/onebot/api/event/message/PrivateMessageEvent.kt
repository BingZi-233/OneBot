package online.bingzi.onebot.api.event.message

import online.bingzi.onebot.api.OneBotAPI

/**
 * 私聊消息事件
 *
 * 当机器人收到私聊消息时触发此事件
 * 其他插件可以监听此事件来处理私聊消息
 */
class PrivateMessageEvent(
    time: Long,
    selfId: Long,
    rawData: String,
    subType: String,
    messageId: Int,
    userId: Long,
    message: String,
    font: Int
) : MessageEvent(time, selfId, rawData, "private", subType, messageId, userId, message, font) {

    override fun reply(message: String, autoEscape: Boolean) {
        OneBotAPI.sendPrivateMessage(userId, message, autoEscape) {

        }
    }

    override fun replyWithQuote(message: String, autoEscape: Boolean) {
        val quotedMessage = "[CQ:reply,id=$messageId]$message"
        OneBotAPI.sendPrivateMessage(userId, quotedMessage, autoEscape) {

        }
    }
}