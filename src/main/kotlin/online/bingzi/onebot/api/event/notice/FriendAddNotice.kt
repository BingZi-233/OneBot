package online.bingzi.onebot.api.event.notice

/**
 * 好友添加事件
 * 
 * 当有新好友被添加时触发此事件
 * 其他插件可以监听此事件来处理新好友添加
 */
class FriendAddNotice(
    time: Long,
    selfId: Long,
    rawData: String,
    val userId: Long
) : NoticeEvent(time, selfId, rawData, "friend_add")