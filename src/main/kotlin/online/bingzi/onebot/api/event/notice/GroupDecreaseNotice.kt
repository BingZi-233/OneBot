package online.bingzi.onebot.api.event.notice

/**
 * 群成员减少事件
 * 
 * 当群成员离开或被踢出群聊时触发此事件
 * 其他插件可以监听此事件来处理成员离开
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