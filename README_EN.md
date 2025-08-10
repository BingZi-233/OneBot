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

## 📁 Project Structure

```
OneBot/
├── src/
│   └── main/
│       ├── kotlin/          # Kotlin source code
│       └── resources/       # Resource files
│           ├── config.yml   # Main configuration
│           └── lang/        # Language files
├── build.gradle.kts         # Build configuration
└── settings.gradle.kts      # Project settings
```

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