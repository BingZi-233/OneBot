package online.bingzi.onebot.event.notice

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