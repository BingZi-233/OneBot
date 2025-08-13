# OneBot

<div align="center">

![OneBot](https://img.shields.io/badge/OneBot-v11%2Fv12-blue)
![TabooLib](https://img.shields.io/badge/TabooLib-6.2.3+-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.8--1.21+-orange)
![Java](https://img.shields.io/badge/Java-8+-red)
![Build Status](https://img.shields.io/github/actions/workflow/status/BingZi-233/OneBot/build.yml)
![License](https://img.shields.io/github/license/BingZi-233/OneBot)
![Downloads](https://img.shields.io/github/downloads/BingZi-233/OneBot/total)

一个基于 TabooLib 框架开发的 Minecraft OneBot 协议插件，用于连接 QQ 机器人和 Minecraft 服务器。

**中文文档** | [English](./README_EN.md) | [Wiki](https://github.com/BingZi-233/OneBot/wiki) | [Issues](https://github.com/BingZi-233/OneBot/issues)

</div>

## 🌟 功能特性

- ✅ **完整的 OneBot v11 协议支持** - 兼容主流Bot框架
- ✅ **灵活的 WebSocket 连接** - 支持自定义URL、路径、参数和请求头
- ✅ **预设配置系统** - 一键配置常见Bot（go-cqhttp、Mirai、NoneBot2等）
- ✅ **完整的事件系统** - 消息、通知、请求事件全覆盖
- ✅ **完全异步的 API 接口** - 所有API调用均为异步回调模式，避免阻塞主线程
- ✅ **智能重连机制** - 自动重连、断线恢复
- ✅ **调试和监控** - 完善的日志系统和状态监控
- ✅ **简洁的公共接口** - 方便其他插件集成使用
- ✅ **命令行管理** - 完整的管理命令系统

## 📦 支持的Bot框架

| Bot框架 | 默认端口 | 预设配置 | 连接路径 |
|---------|----------|----------|----------|
| [go-cqhttp](https://github.com/Mrs4s/go-cqhttp) | 5700 | `go-cqhttp` | `/` |
| [Mirai](https://github.com/mamoe/mirai) + mirai-api-http | 8080 | `mirai` | `/all` |
| [NoneBot2](https://github.com/nonebot/nonebot2) | 8080 | `nonebot2` | `/onebot/v11/ws` |
| [LLOneBot](https://github.com/LLOneBot/LLOneBot) | 3001 | `llonebot` | `/` |
| [OpenShamrock](https://github.com/whitechi73/OpenShamrock) | 5800 | - | `/` |
| [Koishi](https://github.com/koishijs/koishi) | 5140 | - | `/onebot` |

## 🚀 快速开始

### 1. 安装插件

将编译好的插件jar文件放入服务器的 `plugins` 目录，启动服务器。

### 2. 配置连接

#### 方法一：使用预设配置（推荐）
```bash
# 查看可用预设
/onebot preset list

# 应用预设配置
/onebot preset apply go-cqhttp
/onebot restart
```

#### 方法二：手动配置
编辑 `plugins/OneBot/config.yml`：

```yaml
# 基础配置示例（go-cqhttp）
onebot:
  url: "ws://localhost:5700"
  token: ""
  
# 高级配置示例（NoneBot2）
onebot:
  url: "ws://localhost:8080"
  custom_path: "/onebot/v11/ws"
  headers:
    Authorization: "Bearer your-token"
```

### 3. 启动连接

```bash
/onebot connect
```

## 🎮 命令使用

| 命令 | 描述 |
|------|------|
| `/onebot help` | 显示帮助信息 |
| `/onebot status` | 显示连接状态和配置信息 |
| `/onebot connect` | 连接到OneBot服务器 |
| `/onebot disconnect` | 断开连接 |
| `/onebot restart` | 重启连接 |
| `/onebot reload` | 重载配置文件 |
| `/onebot preset list` | 显示可用预设配置 |
| `/onebot preset apply <名称>` | 应用预设配置 |
| `/onebot test private <QQ号> [消息]` | 发送测试私聊消息 |
| `/onebot test group <群号> [消息]` | 发送测试群消息 |

## 💻 开发使用

### 监听事件

```kotlin
import online.bingzi.onebot.event.message.GroupMessageEvent
import online.bingzi.onebot.event.message.PrivateMessageEvent
import taboolib.common.platform.event.SubscribeEvent

@SubscribeEvent
fun onGroupMessage(event: GroupMessageEvent) {
    when {
        event.message == "hello" -> {
            event.reply("Hello, world!")
        }
        event.message == "ping" -> {
            event.replyWithAt("pong!")
        }
        event.message.startsWith("echo ") -> {
            val content = event.message.removePrefix("echo ")
            event.replyWithQuote("你说：$content")
        }
    }
}

@SubscribeEvent
fun onPrivateMessage(event: PrivateMessageEvent) {
    if (event.message.contains("帮助")) {
        event.reply("OneBot插件 v1.0\n支持的命令：...")
    }
}
```

### 使用API

```kotlin
import online.bingzi.onebot.api.OneBotAPI

// 发送消息（异步回调模式）
OneBotAPI.sendGroupMessage(群号, "Hello Group!") { success ->
    if (success) {
        println("群消息发送成功")
    } else {
        println("群消息发送失败")
    }
}

OneBotAPI.sendPrivateMessage(QQ号, "Hello Private!") { success ->
    if (success) {
        println("私聊消息发送成功")
    }
}

// 群管理（异步回调）
OneBotAPI.banGroupMember(群号, QQ号, 600) { success ->
    if (success) {
        println("禁言成功")
    }
} // 禁言10分钟

OneBotAPI.kickGroupMember(群号, QQ号, true) { success ->
    if (success) {
        println("踢出成功")
    }
} // 踢出并拒绝加群请求

// 获取信息（异步回调）
OneBotAPI.getFriendList { friendList ->
    if (friendList != null) {
        println("好友列表: $friendList")
    }
}

OneBotAPI.getGroupList { groupList ->
    if (groupList != null) {
        println("群列表: $groupList") 
    }
}

OneBotAPI.getGroupMemberList(群号) { memberList ->
    if (memberList != null) {
        println("群成员列表: $memberList")
    }
}

// 检查连接状态
if (OneBotAPI.isConnected()) {
    // 执行需要连接的操作
}
```

## 🏗️ 架构设计

```
OneBot 插件架构
├── 📁 config/          # 配置管理
│   └── OneBotConfig    # 配置类，支持预设和自定义配置
├── 📁 client/          # WebSocket 连接
│   └── OneBotWebSocketClient  # WebSocket客户端实现
├── 📁 event/           # 事件系统
│   ├── EventFactory   # 事件工厂，解析和分发事件
│   ├── base/          # 基础事件类
│   │   └── OneBotEvent # 事件基类
│   ├── message/       # 消息事件包
│   │   ├── MessageEvent        # 消息事件基类
│   │   ├── PrivateMessageEvent # 私聊消息事件
│   │   └── GroupMessageEvent   # 群消息事件
│   ├── notice/        # 通知事件包
│   │   ├── NoticeEvent         # 通知事件基类
│   │   ├── GroupIncreaseNotice # 群成员增加通知
│   │   ├── GroupDecreaseNotice # 群成员减少通知
│   │   ├── GroupBanNotice      # 群禁言通知
│   │   └── FriendAddNotice     # 好友添加通知
│   └── request/       # 请求事件包
│       ├── RequestEvent        # 请求事件基类
│       ├── FriendRequestEvent  # 加好友请求事件
│       └── GroupRequestEvent   # 加群请求事件
├── 📁 action/         # API 调用
│   └── ActionFactory  # 异步API调用工厂，基于CompletableFuture
├── 📁 api/            # 公共接口
│   └── OneBotAPI     # 异步回调模式的API接口
├── 📁 manager/        # 连接管理
│   └── OneBotManager # 生命周期管理
├── 📁 command/        # 命令系统
│   └── OneBotCommand # 管理命令实现
└── 📁 example/        # 使用示例
```

## 🔧 高级配置

### 完整配置示例

```yaml
onebot:
  # 基础连接配置
  url: "ws://localhost:8080"
  token: "your-access-token"
  bot_id: 123456789
  
  # 自定义配置
  custom_path: "/onebot/v11/ws"
  extra_params:
    version: "11"
    platform: "minecraft"
  headers:
    User-Agent: "OneBot-Minecraft/1.0"
    Authorization: "Bearer your-token"
    
  # 连接设置
  reconnect_interval: 5
  max_reconnect_attempts: -1
  connect_timeout: 10000

events:
  enabled: true
  thread_pool_size: 4

debug:
  enabled: false
  log_raw_messages: false
  log_actions: false
```

### 安全连接配置

```yaml
onebot:
  url: "wss://your-secure-server.com:443"  # WSS加密连接
  token: "your-secure-token"
  headers:
    X-API-Key: "your-api-key"
    Origin: "https://trusted-domain.com"
```

## 📚 事件类型

### 消息事件
- `PrivateMessageEvent` - 私聊消息
- `GroupMessageEvent` - 群消息

### 通知事件  
- `GroupIncreaseNotice` - 群成员增加
- `GroupDecreaseNotice` - 群成员减少
- `GroupBanNotice` - 群禁言事件
- `FriendAddNotice` - 好友添加

### 请求事件
- `FriendRequestEvent` - 加好友请求
- `GroupRequestEvent` - 加群请求

## 📋 构建说明

### 构建发行版本

发行版本用于正常使用，不含 TabooLib 本体：

```bash
./gradlew build
```

### 构建开发版本

开发版本包含 TabooLib 本体，用于开发者使用：

```bash
./gradlew taboolibBuildApi -PDeleteCode
```

> 参数 `-PDeleteCode` 表示移除所有逻辑代码以减少体积。

## 🔗 依赖要求

- **Minecraft 服务器**: Bukkit/Spigot/Paper
- **Java**: 8+ 
- **TabooLib**: 6.2.3+
- **OneBot 协议**: v11 兼容

## 📖 文档链接

- 📑 [配置指南](CONFIG_GUIDE.md) - 详细的配置说明和示例
- 📚 [Wiki文档](wiki/) - 完整的开发文档
- 🔍 [API参考](wiki/API-Reference.md) - API接口文档
- 🎯 [事件指南](wiki/Events-Guide.md) - 事件处理指南

## 🐛 故障排除

### 常见问题

1. **连接失败**
   - 检查Bot服务是否启动
   - 确认URL和端口正确
   - 查看防火墙设置

2. **认证错误**
   - 检查token配置
   - 确认认证方式正确
   - 尝试不同的预设配置

3. **事件不触发**
   - 启用调试模式：`debug.enabled: true`
   - 检查事件处理是否启用
   - 查看控制台日志

4. **消息发送失败**
   - 确认连接状态：`/onebot status`
   - 检查Bot权限设置
   - 启用动作日志：`debug.log_actions: true`

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

### 贡献指南
1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 🙏 致谢

- [cnlimiter/onebot-client](https://github.com/cnlimiter/onebot-client) - 本项目基于该项目重新设计和实现
- [TabooLib](https://github.com/TabooLib/taboolib) - 强大的 Minecraft 插件开发框架
- [OneBot](https://onebot.dev/) - 聊天机器人应用接口标准
- 所有贡献者和使用者

---

<div align="center">

**[⭐ Star](https://github.com/BingZi-233/OneBot) | [📖 Wiki](wiki/) | [🐛 Issues](https://github.com/BingZi-233/OneBot/issues)**

Made with ❤️ using TabooLib

</div>