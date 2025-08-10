# 基础机器人示例

本文档将通过完整的示例，展示如何使用OneBot插件创建一个基础的聊天机器人。

## 🎯 功能目标

我们要创建一个具备以下功能的机器人：
- 响应问候语
- 处理简单命令
- 提供服务器信息
- 群聊互动功能
- 私聊帮助系统

## 📁 项目结构

```
your-plugin/
├── src/main/kotlin/
│   └── com/yourname/bot/
│       ├── SimpleBotPlugin.kt
│       ├── handlers/
│       │   ├── MessageHandler.kt
│       │   ├── CommandHandler.kt
│       │   └── NoticeHandler.kt
│       └── utils/
│           └── BotUtils.kt
└── plugin.yml
```

## 🚀 完整示例代码

### 1. 主插件类

```kotlin
// SimpleBotPlugin.kt
package com.yourname.bot

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console

object SimpleBotPlugin : Plugin() {
    
    override fun onEnable() {
        console().sendMessage("§a[SimpleBot] 机器人插件已启动!")
        console().sendMessage("§e[SimpleBot] 请确保OneBot插件已正确配置")
    }
    
    override fun onDisable() {
        console().sendMessage("§c[SimpleBot] 机器人插件已关闭!")
    }
}
```

### 2. 消息处理器

```kotlin
// MessageHandler.kt
package com.yourname.bot.handlers

import online.bingzi.onebot.api.OneBotAPI
import online.bingzi.onebot.event.message.GroupMessageEvent
import online.bingzi.onebot.event.message.PrivateMessageEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MessageHandler {
    
    private val greetings = listOf("你好", "hi", "hello", "早上好", "晚上好", "下午好")
    private val responses = listOf(
        "你好！很高兴见到你~",
        "嗨！有什么需要帮助的吗？", 
        "你好呀！今天过得怎么样？",
        "Hello! 需要什么帮助吗？"
    )
    
    /**
     * 处理群消息
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    fun onGroupMessage(event: GroupMessageEvent) {
        val message = event.message.trim().lowercase()
        
        // 记录消息（可选）
        console().sendMessage("§7[群消息] ${event.groupId}:${event.userId} -> ${event.message}")
        
        // 问候回复
        if (greetings.any { greeting -> greeting in message }) {
            val response = responses.random()
            event.replyWithAt(response)
            return
        }
        
        // 关键词回复
        when {
            "时间" in message || "几点" in message -> {
                val now = LocalDateTime.now()
                val timeStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                event.reply("现在是 $timeStr")
            }
            
            "日期" in message || "今天" in message -> {
                val now = LocalDateTime.now()
                val dateStr = now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
                event.reply("今天是 $dateStr")
            }
            
            "天气" in message -> {
                event.reply("今天天气很好呢！☀️ (这是模拟回复)")
            }
            
            "机器人" in message && "介绍" in message -> {
                event.reply("""
                    🤖 我是基于OneBot的Minecraft机器人
                    
                    功能包括：
                    • 聊天互动
                    • 服务器信息查询  
                    • 简单命令处理
                    
                    输入 /help 查看更多命令
                """.trimIndent())
            }
            
            message.contains("ping") -> {
                event.replyWithAt("pong! 🏓")
            }
        }
    }
    
    /**
     * 处理私聊消息
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    fun onPrivateMessage(event: PrivateMessageEvent) {
        val message = event.message.trim()
        
        console().sendMessage("§7[私聊] ${event.userId} -> $message")
        
        // 私聊都回复帮助信息
        when {
            "帮助" in message || "help" in message || message == "?" -> {
                event.reply(getHelpMessage())
            }
            
            "状态" in message || "status" in message -> {
                val connected = OneBotAPI.isConnected()
                event.reply("机器人状态: ${if (connected) "✅ 在线" else "❌ 离线"}")
            }
            
            greetings.any { it in message.lowercase() } -> {
                event.reply("你好！欢迎私聊我~\n输入'帮助'查看可用功能")
            }
            
            else -> {
                event.reply("收到你的消息了！\n输入'帮助'查看可用功能")
            }
        }
    }
    
    private fun getHelpMessage(): String {
        return """
            🤖 机器人帮助文档
            
            === 群聊功能 ===
            • 问候语自动回复
            • "时间" - 查看当前时间
            • "日期" - 查看当前日期  
            • "天气" - 查看天气信息
            • "机器人介绍" - 机器人简介
            • "ping" - 连通性测试
            
            === 私聊功能 ===
            • "帮助" - 显示此帮助
            • "状态" - 查看机器人状态
            
            === 命令功能 ===  
            • /help - 显示帮助
            • /time - 当前时间
            • /server - 服务器信息
            • /players - 在线玩家
            
            有问题请联系管理员！
        """.trimIndent()
    }
}
```

### 3. 命令处理器

```kotlin
// CommandHandler.kt
package com.yourname.bot.handlers

import online.bingzi.onebot.event.message.GroupMessageEvent
import online.bingzi.onebot.event.message.PrivateMessageEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.platform.BukkitPlugin
import org.bukkit.Bukkit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CommandHandler {
    
    /**
     * 处理群聊命令
     */
    @SubscribeEvent
    fun onGroupCommand(event: GroupMessageEvent) {
        val message = event.message.trim()
        
        if (!message.startsWith("/")) return
        
        val args = message.split(" ")
        val command = args[0].lowercase()
        
        when (command) {
            "/help" -> {
                event.reply("""
                    📋 可用命令:
                    /help - 显示此帮助
                    /time - 当前服务器时间
                    /server - 服务器信息
                    /players - 在线玩家列表
                    /ping - 延迟测试
                """.trimIndent())
            }
            
            "/time" -> {
                val now = LocalDateTime.now()
                val timeStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                event.reply("🕐 服务器时间: $timeStr")
            }
            
            "/server" -> {
                try {
                    val serverName = Bukkit.getServerName()
                    val version = Bukkit.getVersion()
                    val onlinePlayers = Bukkit.getOnlinePlayers().size
                    val maxPlayers = Bukkit.getMaxPlayers()
                    
                    event.reply("""
                        🖥️ 服务器信息:
                        名称: $serverName
                        版本: $version  
                        在线人数: $onlinePlayers/$maxPlayers
                    """.trimIndent())
                } catch (e: Exception) {
                    event.reply("❌ 获取服务器信息失败")
                }
            }
            
            "/players" -> {
                try {
                    val players = Bukkit.getOnlinePlayers()
                    if (players.isEmpty()) {
                        event.reply("😴 当前没有玩家在线")
                    } else {
                        val playerNames = players.take(10).joinToString(", ") { it.name }
                        val total = players.size
                        event.reply("👥 在线玩家 ($total): $playerNames${if (total > 10) "..." else ""}")
                    }
                } catch (e: Exception) {
                    event.reply("❌ 获取玩家列表失败")
                }
            }
            
            "/ping" -> {
                val startTime = System.currentTimeMillis()
                // 模拟延迟测试
                event.reply("🏓 Pong! 延迟: ${System.currentTimeMillis() - startTime}ms")
            }
            
            else -> {
                event.reply("❓ 未知命令，输入 /help 查看可用命令")
            }
        }
    }
    
    /**
     * 处理私聊命令
     */
    @SubscribeEvent
    fun onPrivateCommand(event: PrivateMessageEvent) {
        val message = event.message.trim()
        
        if (!message.startsWith("/")) return
        
        // 私聊命令处理逻辑与群聊相同
        // 可以根据需要添加私聊特有的命令
        when (message.lowercase()) {
            "/admin" -> {
                // 管理员功能示例
                event.reply("🔐 管理员功能正在开发中...")
            }
        }
    }
}
```

### 4. 通知处理器

```kotlin
// NoticeHandler.kt
package com.yourname.bot.handlers

import online.bingzi.onebot.api.OneBotAPI
import online.bingzi.onebot.event.notice.GroupIncreaseNotice
import online.bingzi.onebot.event.notice.GroupDecreaseNotice
import online.bingzi.onebot.event.notice.FriendAddNotice
import online.bingzi.onebot.event.request.FriendRequestEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console

object NoticeHandler {
    
    /**
     * 新成员加入群聊
     */
    @SubscribeEvent
    fun onGroupIncrease(event: GroupIncreaseNotice) {
        console().sendMessage("§a[群通知] 新成员 ${event.userId} 加入群 ${event.groupId}")
        
        val welcomeMessages = listOf(
            "🎉 欢迎新朋友！请多多关照~",
            "👋 欢迎加入我们！有问题随时问哦",
            "🌟 欢迎新成员！记得看看群公告",
            "✨ 又来新朋友啦！大家欢迎~"
        )
        
        val welcomeMsg = "[CQ:at,qq=${event.userId}] ${welcomeMessages.random()}"
        OneBotAPI.sendGroupMessage(event.groupId, welcomeMsg)
    }
    
    /**
     * 成员离开群聊
     */
    @SubscribeEvent
    fun onGroupDecrease(event: GroupDecreaseNotice) {
        when (event.subType) {
            "leave" -> {
                console().sendMessage("§e[群通知] 成员 ${event.userId} 主动退出群 ${event.groupId}")
                // 可选：发送离开消息
                // OneBotAPI.sendGroupMessage(event.groupId, "有成员离开了群聊...")
            }
            "kick" -> {
                console().sendMessage("§c[群通知] 成员 ${event.userId} 被踢出群 ${event.groupId}")
            }
            "kick_me" -> {
                console().sendMessage("§c[群通知] 机器人被踢出群 ${event.groupId}")
            }
        }
    }
    
    /**
     * 新增好友
     */
    @SubscribeEvent
    fun onFriendAdd(event: FriendAddNotice) {
        console().sendMessage("§a[好友通知] 新增好友: ${event.userId}")
        
        // 发送欢迎消息
        val welcomeMsg = """
            👋 很高兴成为朋友！
            
            我是Minecraft服务器的机器人，可以：
            • 提供服务器信息
            • 处理简单命令
            • 聊天互动
            
            输入 "帮助" 查看详细功能~
        """.trimIndent()
        
        OneBotAPI.sendPrivateMessage(event.userId, welcomeMsg)
    }
    
    /**
     * 好友请求（示例，实际处理需要完善请求处理功能）
     */
    @SubscribeEvent
    fun onFriendRequest(event: FriendRequestEvent) {
        console().sendMessage("§b[请求通知] 收到好友申请: ${event.userId}, 消息: ${event.comment}")
        
        // 自动同意包含特定关键词的好友请求
        val autoApproveKeywords = listOf("minecraft", "mc", "服务器", "玩家")
        val shouldApprove = autoApproveKeywords.any { keyword -> 
            keyword in event.comment.lowercase() 
        }
        
        if (shouldApprove) {
            console().sendMessage("§a[请求通知] 自动同意好友请求: ${event.userId}")
            // TODO: 实现自动同意功能
            // event.approve("欢迎！")
        } else {
            console().sendMessage("§e[请求通知] 好友请求需要手动处理: ${event.userId}")
        }
    }
}
```

### 5. 工具类

```kotlin
// BotUtils.kt
package com.yourname.bot.utils

import online.bingzi.onebot.api.OneBotAPI
import org.bukkit.Bukkit
import java.text.SimpleDateFormat
import java.util.*

object BotUtils {
    
    /**
     * 格式化时间
     */
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
    
    /**
     * 获取服务器状态信息
     */
    fun getServerStatus(): String {
        return try {
            val onlineCount = Bukkit.getOnlinePlayers().size
            val maxPlayers = Bukkit.getMaxPlayers()
            val serverVersion = Bukkit.getVersion()
            
            """
            🖥️ 服务器状态:
            版本: $serverVersion
            在线: $onlineCount/$maxPlayers
            运行时间: ${getUptime()}
            """.trimIndent()
        } catch (e: Exception) {
            "❌ 无法获取服务器状态"
        }
    }
    
    /**
     * 获取服务器运行时间（简化版）
     */
    private fun getUptime(): String {
        val runtime = Runtime.getRuntime()
        val uptime = System.currentTimeMillis() - runtime.totalMemory()
        val seconds = (uptime / 1000) % 60
        val minutes = (uptime / (1000 * 60)) % 60
        val hours = uptime / (1000 * 60 * 60)
        
        return "${hours}小时${minutes}分钟"
    }
    
    /**
     * 安全发送群消息（带重试）
     */
    fun safeSendGroupMessage(groupId: Long, message: String, maxRetries: Int = 3): Boolean {
        repeat(maxRetries) { attempt ->
            if (OneBotAPI.sendGroupMessage(groupId, message)) {
                return true
            }
            if (attempt < maxRetries - 1) {
                Thread.sleep(1000) // 等待1秒后重试
            }
        }
        return false
    }
    
    /**
     * 检查消息是否包含敏感词（示例）
     */
    fun containsBadWords(message: String): Boolean {
        val badWords = listOf("广告", "代理", "色情") // 示例敏感词
        return badWords.any { word -> word in message.lowercase() }
    }
    
    /**
     * 生成随机回复
     */
    fun getRandomReply(type: String): String {
        val replies = when (type) {
            "greeting" -> listOf(
                "你好！",
                "嗨~",
                "很高兴见到你！",
                "欢迎！"
            )
            "thanks" -> listOf(
                "不客气！",
                "很高兴能帮到你~",
                "小事一桩！",
                "😊"
            )
            "goodbye" -> listOf(
                "再见！",
                "祝你愉快！", 
                "下次见~",
                "拜拜！"
            )
            else -> listOf("好的！")
        }
        return replies.random()
    }
}
```

## 🎮 使用方法

### 1. 安装和配置

1. 将代码编译成插件jar文件
2. 放入服务器plugins目录
3. 确保OneBot插件已正确配置
4. 重启服务器

### 2. 测试功能

在QQ中测试以下功能：

**群聊测试**:
- 发送"你好"看是否回复
- 发送"/help"查看命令帮助
- 发送"时间"查询当前时间
- 发送"/server"查看服务器信息

**私聊测试**:
- 私聊机器人发送"帮助"
- 发送"状态"查看机器人状态

## 🔧 扩展功能

基于这个示例，你可以添加更多功能：

### 数据持久化
```kotlin
// 添加玩家数据记录
@SubscribeEvent
fun onPlayerMessage(event: GroupMessageEvent) {
    // 记录玩家活跃度
    PlayerDataManager.updateActivity(event.userId)
}
```

### 定时任务
```kotlin
// 定时发送服务器状态
submit(period = 20 * 60 * 5) { // 每5分钟
    val status = BotUtils.getServerStatus()
    OneBotAPI.sendGroupMessage(主群号, "📊 定时状态报告:\n$status")
}
```

### 游戏内联动
```kotlin
// 监听玩家进出服务器
@SubscribeEvent
fun onPlayerJoin(event: PlayerJoinEvent) {
    val message = "🎮 玩家 ${event.player.name} 加入了服务器"
    OneBotAPI.sendGroupMessage(游戏群号, message)
}
```

## 📚 更多示例

- [群管理机器人示例](Examples-Group-Manager.md) - 更复杂的群管理功能
- [游戏联动示例](Examples-Game-Integration.md) - 游戏内外信息同步
- [API参考文档](API-Reference.md) - 完整API使用说明