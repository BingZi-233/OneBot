package online.bingzi.onebot.event.notice

/**
 * 好友添加事件
 */
class FriendAddNotice(
    time: Long,
    selfId: Long,
    rawData: String,
    val userId: Long
) : NoticeEvent(time, selfId, rawData, "friend_add")