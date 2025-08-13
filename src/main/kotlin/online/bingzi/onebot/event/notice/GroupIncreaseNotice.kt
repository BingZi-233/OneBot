package online.bingzi.onebot.event.notice

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