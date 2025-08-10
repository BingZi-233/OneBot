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

/**
 * 群成员增加事件
 */
class GroupIncreaseNotice(
    time: Long,
    selfId: Long,
    rawData: String,
    val subType: String,
    val groupId: Long,
    val operatorId: Long,
    val userId: Long
) : NoticeEvent(time, selfId, rawData, "group_increase")

/**
 * 群成员减少事件
 */
class GroupDecreaseNotice(
    time: Long,
    selfId: Long,
    rawData: String,
    val subType: String,
    val groupId: Long,
    val operatorId: Long,
    val userId: Long
) : NoticeEvent(time, selfId, rawData, "group_decrease")

/**
 * 群禁言事件
 */
class GroupBanNotice(
    time: Long,
    selfId: Long,
    rawData: String,
    val subType: String,
    val groupId: Long,
    val operatorId: Long,
    val userId: Long,
    val duration: Int
) : NoticeEvent(time, selfId, rawData, "group_ban")

/**
 * 好友添加事件
 */
class FriendAddNotice(
    time: Long,
    selfId: Long,
    rawData: String,
    val userId: Long
) : NoticeEvent(time, selfId, rawData, "friend_add")