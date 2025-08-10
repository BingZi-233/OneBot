# 事件处理指南

OneBot插件提供了完整的事件系统，让你可以监听和处理各种QQ事件。

## 📋 事件概览

### 事件层次结构
```
OneBotEvent (基类)
├── MessageEvent (消息事件)
│   ├── PrivateMessageEvent (私聊消息)
│   └── GroupMessageEvent (群消息)
├── NoticeEvent (通知事件)
│   ├── GroupIncreaseNotice (群成员增加)
│   ├── GroupDecreaseNotice (群成员减少)
│   ├── GroupBanNotice (群禁言)
│   └── FriendAddNotice (好友添加)
└── RequestEvent (请求事件)
    ├── FriendRequestEvent (加好友请求)
    └── GroupRequestEvent (加群请求)
```

## 🎯 事件监听

### 基础监听
使用 `@SubscribeEvent` 注解监听事件：

```kotlin
import online.bingzi.onebot.event.message.GroupMessageEvent
import taboolib.common.platform.event.SubscribeEvent

@SubscribeEvent
fun onGroupMessage(event: GroupMessageEvent) {
    // 处理群消息事件
}
```

### 事件优先级
可以设置事件处理的优先级：

```kotlin
import taboolib.common.platform.event.EventPriority

@SubscribeEvent(priority = EventPriority.HIGH)
fun onHighPriorityEvent(event: GroupMessageEvent) {
    // 高优先级处理
}

@SubscribeEvent(priority = EventPriority.LOW) 
fun onLowPriorityEvent(event: GroupMessageEvent) {
    // 低优先级处理
}
```

**优先级顺序**: `HIGHEST` > `HIGH` > `NORMAL` > `LOW` > `LOWEST`

### 事件取消
可以取消事件的继续传播：

```kotlin
@SubscribeEvent
fun onGroupMessage(event: GroupMessageEvent) {
    if (event.message.contains("禁言")) {
        event.isCancelled = true // 取消事件，阻止后续处理器执行
        return
    }
}
```

---

## 💬 消息事件

### 私聊消息事件
```kotlin
import online.bingzi.onebot.event.message.PrivateMessageEvent

@SubscribeEvent
fun onPrivateMessage(event: PrivateMessageEvent) {
    println("收到私聊消息:")
    println("  发送者: ${event.userId}")
    println("  消息内容: ${event.message}")
    println("  消息ID: ${event.messageId}")
    println("  时间戳: ${event.time}")
    
    // 回复消息
    event.reply("收到你的消息了！")
    
    // 带引用回复
    event.replyWithQuote("这是引用回复")
}
```

**事件属性**:
- `userId: Long` - 发送者QQ号
- `message: String` - 消息内容
- `messageId: Int` - 消息ID
- `time: Long` - 事件时间戳
- `selfId: Long` - 机器人QQ号
- `subType: String` - 子类型（friend, group, other）
- `font: Int` - 字体ID

### 群消息事件
```kotlin
import online.bingzi.onebot.event.message.GroupMessageEvent

@SubscribeEvent  
fun onGroupMessage(event: GroupMessageEvent) {
    println("收到群消息:")
    println("  群号: ${event.groupId}")
    println("  发送者: ${event.userId}")
    println("  消息内容: ${event.message}")
    
    // 普通回复
    event.reply("群消息回复")
    
    // @发送者回复
    event.replyWithAt("你好！")
    
    // 引用回复
    event.replyWithQuote("收到消息")
}
```

**额外属性**:
- `groupId: Long` - 群号
- `anonymous: Any?` - 匿名信息

### 消息处理示例

#### 命令处理器
```kotlin
@SubscribeEvent
fun onCommand(event: GroupMessageEvent) {
    val message = event.message.trim()
    
    when {
        message == "/help" -> {
            event.reply("""
                可用命令:
                /help - 显示帮助
                /time - 显示当前时间
                /weather - 查看天气
            """.trimIndent())
        }
        
        message == "/time" -> {
            val time = java.time.LocalDateTime.now()
            event.reply("当前时间: $time")
        }
        
        message.startsWith("/say ") -> {
            val content = message.removePrefix("/say ")
            event.reply("你让我说: $content")
        }
    }
}
```

#### 关键词回复
```kotlin
@SubscribeEvent
fun onKeywordReply(event: GroupMessageEvent) {
    val message = event.message.lowercase()
    
    when {
        "你好" in message || "hello" in message -> {
            event.replyWithAt("你好！很高兴认识你~")
        }
        
        "天气" in message -> {
            event.reply("今天天气很好呢！☀️")
        }
        
        "晚安" in message -> {
            event.reply("晚安~好梦！🌙")
        }
    }
}
```

#### 消息过滤器
```kotlin
@SubscribeEvent(priority = EventPriority.HIGHEST)
fun messageFilter(event: GroupMessageEvent) {
    val message = event.message
    
    // 过滤广告
    val adKeywords = listOf("加群", "广告", "代理")
    if (adKeywords.any { it in message }) {
        event.isCancelled = true
        // 可选：撤回消息
        OneBotAPI.deleteMessage(event.messageId)
        return
    }
    
    // 过滤长消息
    if (message.length > 500) {
        event.isCancelled = true
        event.reply("消息太长了，请简化一下~")
        return
    }
}
```

---

## 📢 通知事件

### 群成员增加事件
```kotlin
import online.bingzi.onebot.event.notice.GroupIncreaseNotice

@SubscribeEvent
fun onGroupIncrease(event: GroupIncreaseNotice) {
    println("新成员加入:")
    println("  群号: ${event.groupId}")
    println("  新成员: ${event.userId}")
    println("  操作者: ${event.operatorId}")
    println("  类型: ${event.subType}") // approve(同意入群), invite(邀请入群)
    
    // 发送欢迎消息
    val welcomeMsg = when (event.subType) {
        "approve" -> "[CQ:at,qq=${event.userId}] 欢迎新成员！请阅读群公告~"
        "invite" -> "[CQ:at,qq=${event.userId}] 欢迎朋友的邀请加入！"
        else -> "欢迎新成员！"
    }
    
    OneBotAPI.sendGroupMessage(event.groupId, welcomeMsg)
}
```

### 群成员减少事件
```kotlin
import online.bingzi.onebot.event.notice.GroupDecreaseNotice

@SubscribeEvent
fun onGroupDecrease(event: GroupDecreaseNotice) {
    when (event.subType) {
        "leave" -> {
            println("成员 ${event.userId} 主动退群")
        }
        "kick" -> {
            println("成员 ${event.userId} 被 ${event.operatorId} 踢出")
        }
        "kick_me" -> {
            println("机器人被踢出群 ${event.groupId}")
        }
    }
}
```

### 群禁言事件
```kotlin
import online.bingzi.onebot.event.notice.GroupBanNotice

@SubscribeEvent
fun onGroupBan(event: GroupBanNotice) {
    when (event.subType) {
        "ban" -> {
            val duration = event.duration
            if (duration > 0) {
                println("${event.userId} 被禁言 $duration 秒")
            }
        }
        "lift_ban" -> {
            println("${event.userId} 被解除禁言")
        }
    }
}
```

### 好友添加事件
```kotlin
import online.bingzi.onebot.event.notice.FriendAddNotice

@SubscribeEvent
fun onFriendAdd(event: FriendAddNotice) {
    println("新增好友: ${event.userId}")
    
    // 发送欢迎消息
    OneBotAPI.sendPrivateMessage(
        event.userId,
        "很高兴成为朋友！有什么需要帮助的可以随时找我~"
    )
}
```

---

## 🙋 请求事件

### 好友请求事件
```kotlin
import online.bingzi.onebot.event.request.FriendRequestEvent

@SubscribeEvent
fun onFriendRequest(event: FriendRequestEvent) {
    println("收到加好友请求:")
    println("  申请人: ${event.userId}")
    println("  验证消息: ${event.comment}")
    println("  请求标识: ${event.flag}")
    
    // 自动同意好友请求（谨慎使用）
    if (event.comment.contains("Minecraft") || event.comment.contains("服务器")) {
        // TODO: 实现好友请求处理
        // event.approve("欢迎！")
        println("自动同意好友请求")
    } else {
        println("需要手动处理好友请求")
    }
}
```

### 加群请求事件
```kotlin
import online.bingzi.onebot.event.request.GroupRequestEvent

@SubscribeEvent
fun onGroupRequest(event: GroupRequestEvent) {
    println("收到加群请求:")
    println("  群号: ${event.groupId}")
    println("  申请人: ${event.userId}")
    println("  类型: ${event.subType}") // add(申请加群), invite(邀请入群)
    println("  验证消息: ${event.comment}")
    
    when (event.subType) {
        "add" -> {
            // 处理加群申请
            if (isValidJoinRequest(event.comment)) {
                // TODO: 实现群请求处理
                // event.approve()
                println("自动同意加群申请")
            } else {
                // event.reject("请填写正确的验证信息")
                println("拒绝加群申请")
            }
        }
        "invite" -> {
            // 处理入群邀请
            println("收到入群邀请，自动同意")
            // event.approve()
        }
    }
}

fun isValidJoinRequest(comment: String): Boolean {
    val validKeywords = listOf("Minecraft", "MC", "服务器")
    return validKeywords.any { it in comment }
}
```

---

## 🛠️ 高级事件处理

### 事件链式处理
```kotlin
class MessageProcessor {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun preProcess(event: GroupMessageEvent) {
        // 预处理：记录消息
        logMessage(event)
        
        // 预处理：检查权限
        if (!hasPermission(event.userId, "chat")) {
            event.isCancelled = true
            return
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL)
    fun process(event: GroupMessageEvent) {
        // 主要处理逻辑
        handleCommand(event)
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    fun postProcess(event: GroupMessageEvent) {
        // 后处理：统计消息
        updateStatistics(event)
    }
}
```

### 条件监听
```kotlin
@SubscribeEvent
fun onSpecificGroupMessage(event: GroupMessageEvent) {
    // 只处理特定群的消息
    val allowedGroups = listOf(123456L, 789012L)
    if (event.groupId !in allowedGroups) {
        return
    }
    
    // 只处理特定用户的消息
    val adminUsers = listOf(111111L, 222222L)
    if (event.userId !in adminUsers) {
        return
    }
    
    // 处理管理员在特定群的消息
    handleAdminCommand(event)
}
```

### 异步事件处理
```kotlin
import taboolib.common.platform.function.submit

@SubscribeEvent
fun onAsyncGroupMessage(event: GroupMessageEvent) {
    // 需要长时间处理的逻辑，使用异步
    submit(async = true) {
        val result = processLongRunningTask(event.message)
        event.reply("处理结果: $result")
    }
}
```

### 事件数据持久化
```kotlin
@SubscribeEvent  
fun onMessageForStats(event: GroupMessageEvent) {
    submit(async = true) {
        // 保存消息统计
        saveMessageStats(
            groupId = event.groupId,
            userId = event.userId,
            messageLength = event.message.length,
            timestamp = event.time
        )
    }
}

data class MessageStats(
    val groupId: Long,
    val userId: Long, 
    val messageLength: Int,
    val timestamp: Long
)
```

## 📊 事件监控和调试

### 事件日志
```kotlin
@SubscribeEvent(priority = EventPriority.MONITOR)
fun eventLogger(event: OneBotEvent) {
    if (OneBotConfig.debugEnabled) {
        console().sendMessage("""
            §7[OneBot Event] ${event::class.simpleName}
            §7  Time: ${event.time}
            §7  SelfId: ${event.selfId}
            §7  Raw: ${event.rawData.take(100)}...
        """.trimIndent())
    }
}
```

### 性能监控
```kotlin
@SubscribeEvent(priority = EventPriority.HIGHEST)
fun performanceMonitor(event: OneBotEvent) {
    val startTime = System.currentTimeMillis()
    
    // 使用最低优先级的监听器测量处理时间
    submit(delay = 1L) {
        val processingTime = System.currentTimeMillis() - startTime
        if (processingTime > 100) { // 超过100ms警告
            console().sendMessage("§c[OneBot] 事件处理耗时过长: ${processingTime}ms")
        }
    }
}
```

## 📚 更多示例

查看以下文档获取更多事件处理示例：
- [基础机器人示例](Examples-Basic-Bot.md)
- [群管理机器人示例](Examples-Group-Manager.md)
- [游戏联动示例](Examples-Game-Integration.md)