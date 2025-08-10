package online.bingzi.onebot.event.base

import taboolib.platform.type.BukkitProxyEvent

/**
 * OneBot 事件基类
 * 所有 OneBot 事件都应继承此类
 */
abstract class OneBotEvent : BukkitProxyEvent() {

    /**
     * 事件时间戳
     */
    abstract val time: Long

    /**
     * 事件类型
     */
    abstract val postType: String

    /**
     * 事件的原始 JSON 数据
     */
    abstract val rawData: String

    /**
     * 机器人 QQ 号
     */
    abstract val selfId: Long

    override val allowCancelled: Boolean = true
}