package online.bingzi.onebot.event.notice

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