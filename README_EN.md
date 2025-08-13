# OneBot

<div align="center">

![OneBot](https://img.shields.io/badge/OneBot-v11%2Fv12-blue)
![TabooLib](https://img.shields.io/badge/TabooLib-6.2.3+-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.8--1.21+-orange)
![Java](https://img.shields.io/badge/Java-8+-red)
![Build Status](https://img.shields.io/github/actions/workflow/status/BingZi-233/OneBot/build.yml)
![License](https://img.shields.io/github/license/BingZi-233/OneBot)
![Downloads](https://img.shields.io/github/downloads/BingZi-233/OneBot/total)

A powerful Minecraft server plugin that bridges OneBot protocol implementations with your game server.

[中文文档](./README.md) | **English** | [Wiki](https://github.com/BingZi-233/OneBot/wiki) | [Issues](https://github.com/BingZi-233/OneBot/issues)

</div>

## ✨ Features

- 🔌 **Protocol Support** - Full support for OneBot v11/v12 protocols
- 🎮 **Wide Compatibility** - Works with Minecraft 1.8-1.21+ servers
- 🚀 **High Performance** - Built on TabooLib framework for optimal performance
- 🔧 **Easy Configuration** - Pre-configured templates for popular OneBot implementations
- ⚡ **Fully Async APIs** - All API calls use async callback patterns to prevent main thread blocking
- 📊 **Metrics Integration** - Built-in bStats metrics for usage analytics
- 🌍 **Internationalization** - Multi-language support with i18n
- 🔄 **Real-time Sync** - Bidirectional message forwarding between game and chat platforms
- 🎨 **Rich Text Support** - Full Minecraft chat component support with formatting

## 📋 Requirements

- **Java**: 8 or higher
- **Server**: Bukkit/Spigot/Paper 1.8-1.21+
- **Memory**: Minimum 2GB recommended
- **OneBot Implementation**: Any OneBot v11/v12 compatible server (e.g., go-cqhttp, Lagrange.OneBot)

## 🚀 Quick Start

### Installation

1. **Download the Plugin**
   - Get the latest release from [Releases](https://github.com/BingZi-233/OneBot/releases)
   - Choose the file: `OneBot-x.x.x.jar`

2. **Install on Server**
   ```bash
   # Place the JAR file in your server's plugins directory
   cp OneBot-*.jar /path/to/server/plugins/
   ```

3. **Start/Restart Server**
   ```bash
   # Restart your Minecraft server
   /reload
   # or
   /stop && ./start.sh
   ```

### Configuration

1. **List Available Presets**
   ```bash
   /onebot preset list
   ```

2. **Apply a Preset Configuration**
   ```bash
   # For go-cqhttp
   /onebot preset apply go-cqhttp
   
   # For Lagrange.OneBot
   /onebot preset apply lagrange
   ```

3. **Connect to OneBot Server**
   ```bash
   /onebot connect
   ```

4. **Check Connection Status**
   ```bash
   /onebot status
   ```

## 🏗️ Architecture Design

```
OneBot Plugin Architecture
├── 📁 config/          # Configuration Management
│   └── OneBotConfig    # Configuration class with preset and custom config support
├── 📁 client/          # WebSocket Connection
│   └── OneBotWebSocketClient  # WebSocket client implementation
├── 📁 event/           # Event System
│   ├── EventFactory   # Event factory for parsing and dispatching events
│   ├── base/          # Base event classes
│   │   └── OneBotEvent # Base event class
│   ├── message/       # Message Event Package
│   │   ├── MessageEvent        # Message event base class
│   │   ├── PrivateMessageEvent # Private message event
│   │   └── GroupMessageEvent   # Group message event
│   ├── notice/        # Notice Event Package
│   │   ├── NoticeEvent         # Notice event base class
│   │   ├── GroupIncreaseNotice # Group member increase notification
│   │   ├── GroupDecreaseNotice # Group member decrease notification
│   │   ├── GroupBanNotice      # Group ban notification
│   │   └── FriendAddNotice     # Friend add notification
│   └── request/       # Request Event Package
│       ├── RequestEvent        # Request event base class
│       ├── FriendRequestEvent  # Friend request event
│       └── GroupRequestEvent   # Group request event
├── 📁 action/         # API Calls
│   └── ActionFactory  # Async API call factory based on CompletableFuture
├── 📁 api/            # Public Interface
│   └── OneBotAPI     # Async callback mode API interface
├── 📁 manager/        # Connection Management
│   └── OneBotManager # Lifecycle management
├── 📁 command/        # Command System
│   └── OneBotCommand # Management command implementation
└── 📁 example/        # Usage Examples
```

## 💻 Development Usage

### Listening to Events

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
            event.replyWithQuote("You said: $content")
        }
    }
}

@SubscribeEvent
fun onPrivateMessage(event: PrivateMessageEvent) {
    if (event.message.contains("help")) {
        event.reply("OneBot Plugin v1.0\nSupported commands: ...")
    }
}
```

### Using API

```kotlin
import online.bingzi.onebot.api.OneBotAPI

// Send messages (async callback mode)
OneBotAPI.sendGroupMessage(groupId, "Hello Group!") { success ->
    if (success) {
        println("Group message sent successfully")
    } else {
        println("Failed to send group message")
    }
}

OneBotAPI.sendPrivateMessage(qqNumber, "Hello Private!") { success ->
    if (success) {
        println("Private message sent successfully")
    }
}

// Group management (async callback)
OneBotAPI.banGroupMember(groupId, qqNumber, 600) { success ->
    if (success) {
        println("Ban successful")
    }
} // Ban for 10 minutes

OneBotAPI.kickGroupMember(groupId, qqNumber, true) { success ->
    if (success) {
        println("Kick successful")
    }
} // Kick and reject add requests

// Get information (async callback)
OneBotAPI.getFriendList { friendList ->
    if (friendList != null) {
        println("Friend list: $friendList")
    }
}

OneBotAPI.getGroupList { groupList ->
    if (groupList != null) {
        println("Group list: $groupList") 
    }
}

OneBotAPI.getGroupMemberList(groupId) { memberList ->
    if (memberList != null) {
        println("Group member list: $memberList")
    }
}

// Check connection status
if (OneBotAPI.isConnected()) {
    // Execute operations that require connection
}
```

## 📚 Event Types

### Message Events
- `PrivateMessageEvent` - Private message
- `GroupMessageEvent` - Group message

### Notice Events  
- `GroupIncreaseNotice` - Group member increase
- `GroupDecreaseNotice` - Group member decrease
- `GroupBanNotice` - Group ban event
- `FriendAddNotice` - Friend add

### Request Events
- `FriendRequestEvent` - Friend request
- `GroupRequestEvent` - Group request

## 🔧 Development

### Building from Source

```bash
# Clone the repository
git clone https://github.com/BingZi-233/OneBot.git
cd OneBot

# Build the plugin
./gradlew build

# The output JAR will be in build/libs/
```

### Development Setup

For developers who want to use OneBot as a dependency:

```bash
# Build API JAR (without implementation)
./gradlew taboolibBuildApi -PDeleteCode
```

### Maven Dependency

```xml
<dependency>
    <groupId>online.bingzi</groupId>
    <artifactId>onebot</artifactId>
    <version>VERSION</version>
</dependency>
```

### Gradle Dependency

```kotlin
dependencies {
    implementation("online.bingzi:onebot:VERSION")
}
```

## 🌐 Supported OneBot Implementations

| Implementation | Protocol Version | Status |
|----------------|------------------|--------|
| go-cqhttp | v11 | ✅ Fully Supported |
| Lagrange.OneBot | v11 | ✅ Fully Supported |
| Shamrock | v11 | ✅ Fully Supported |
| NapCat | v11 | ✅ Fully Supported |
| OneBot v12 | v12 | 🚧 In Development |

## 📖 Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/onebot help` | `onebot.use` | Show help information |
| `/onebot status` | `onebot.use` | Check connection status |
| `/onebot connect` | `onebot.admin` | Connect to OneBot server |
| `/onebot disconnect` | `onebot.admin` | Disconnect from OneBot server |
| `/onebot reload` | `onebot.admin` | Reload configuration |
| `/onebot preset list` | `onebot.admin` | List available presets |
| `/onebot preset apply <name>` | `onebot.admin` | Apply a preset configuration |

## 🔗 Configuration Guide

### Basic Configuration

```yaml
# config.yml
onebot:
  # Connection settings
  connection:
    type: websocket
    host: localhost
    port: 8080
    path: /onebot/v11/ws
    
  # Authentication
  auth:
    enabled: false
    token: your-token-here
    
  # Message forwarding
  forward:
    game-to-bot: true
    bot-to-game: true
    
  # Format settings
  format:
    game-message: "[MC] <{player}> {message}"
    bot-message: "[QQ] <{sender}> {message}"
```

### Advanced Settings

See [Configuration Wiki](https://github.com/BingZi-233/OneBot/wiki/Configuration) for detailed configuration options.

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📊 Metrics

This plugin uses bStats to collect anonymous usage data. You can opt-out in the bStats config file.

View our metrics: [bStats Page](https://bstats.org/plugin/bukkit/OneBot)

## 🐛 Bug Reports & Feature Requests

- **Bug Reports**: [Create an Issue](https://github.com/BingZi-233/OneBot/issues/new?labels=bug)
- **Feature Requests**: [Request a Feature](https://github.com/BingZi-233/OneBot/issues/new?labels=enhancement)
- **Discussions**: [Join Discussions](https://github.com/BingZi-233/OneBot/discussions)

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [cnlimiter/onebot-client](https://github.com/cnlimiter/onebot-client) - This project is redesigned and reimplemented based on this project
- [TabooLib](https://github.com/TabooLib/taboolib) - The powerful framework this plugin is built on
- [OneBot](https://onebot.dev/) - The chat protocol standard
- All contributors and users of this plugin

## 💖 Support

If you find this plugin helpful, please consider:
- ⭐ Starring the repository
- 🐛 Reporting bugs and suggesting features
- 🤝 Contributing to the codebase
- 📢 Sharing with others

---

<div align="center">

**Made with ❤️ by [BingZi-233](https://github.com/BingZi-233)**

[Report Bug](https://github.com/BingZi-233/OneBot/issues/new?labels=bug) · [Request Feature](https://github.com/BingZi-233/OneBot/issues/new?labels=enhancement) · [Join Discussion](https://github.com/BingZi-233/OneBot/discussions)

</div>