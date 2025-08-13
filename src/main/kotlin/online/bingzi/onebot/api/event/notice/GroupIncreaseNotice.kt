package online.bingzi.onebot.api.event.notice

/**
 * 群成员增加事件
 * 
 * 当有新成员加入群聊时触发此事件
 * 其他插件可以监听此事件来处理新成员加入
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