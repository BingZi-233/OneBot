package online.bingzi.onebot.api.event.notice

/**
 * 群禁言事件
 * 
 * 当群成员被禁言或解除禁言时触发此事件
 * 其他插件可以监听此事件来处理群禁言操作
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