# 安装指南

本指南将帮助你快速安装和配置OneBot插件。

## 📋 系统要求

### 必需环境
- **Minecraft服务器**: Bukkit/Spigot/Paper (推荐Paper)
- **Java版本**: Java 8 或更高版本
- **TabooLib**: 6.2.3+ (插件会自动包含)

### 支持的服务器版本
- Minecraft 1.8 - 1.20+ (所有TabooLib支持的版本)

### Bot框架要求
- 支持OneBot v11协议的QQ机器人框架
- WebSocket连接支持

## 📦 下载插件

### 方式一：GitHub Releases (推荐)
1. 访问项目的 [Releases页面](https://github.com/BingZi-233/OneBot/releases)
2. 下载最新版本的 `OneBot-x.x.x.jar` 文件

### 方式二：自行编译
```bash
git clone https://github.com/BingZi-233/OneBot.git
cd OneBot
./gradlew build
```
编译后的插件位于 `build/libs/OneBot-x.x.x.jar`

## 🛠️ 安装步骤

### 1. 安装插件
1. 将下载的 `OneBot-x.x.x.jar` 文件放入服务器的 `plugins/` 目录
2. 重启服务器或使用插件热加载命令

### 2. 检查安装
服务器启动后，检查控制台输出：
```
[INFO] [OneBot] OneBot v1.0.0 Plugin Enabled
[INFO] [OneBot] 正在加载配置文件...
```

如果看到类似输出，说明插件安装成功。

### 3. 初始配置检查
插件首次启动会生成默认配置文件：
- `plugins/OneBot/config.yml` - 主配置文件
- `plugins/OneBot/` - 插件数据目录

## ⚙️ Bot框架安装

选择一个OneBot兼容的QQ机器人框架：

### 推荐框架 - go-cqhttp
go-cqhttp是最受欢迎的OneBot实现，稳定性好。

1. **下载go-cqhttp**
   - 访问 [go-cqhttp Releases](https://github.com/Mrs4s/go-cqhttp/releases)
   - 根据系统下载对应版本

2. **初始配置**
   ```bash
   # Windows
   go-cqhttp.exe
   
   # Linux
   ./go-cqhttp
   ```

3. **配置文件** (`config.yml`)
   ```yaml
   # go-cqhttp基础配置
   account:
     uin: 你的QQ号
     password: '你的QQ密码'
     
   servers:
     - ws-reverse:
         universal: ws://127.0.0.1:5700/
   ```

### 其他框架选择

#### Mirai + mirai-api-http
适合Java开发者，功能丰富：
- [Mirai](https://github.com/mamoe/mirai)
- [mirai-api-http](https://github.com/project-mirai/mirai-api-http)

#### NoneBot2
Python生态，插件丰富：
- [NoneBot2](https://github.com/nonebot/nonebot2)

#### LLOneBot
基于QQNT，新兴选择：
- [LLOneBot](https://github.com/LLOneBot/LLOneBot)

## 🔧 快速配置

### 使用预设配置 (推荐)
插件提供了常见Bot的预设配置：

1. **查看可用预设**
   ```
   /onebot preset list
   ```

2. **应用预设**
   ```
   /onebot preset apply go-cqhttp
   ```

3. **重启连接**
   ```
   /onebot restart
   ```

### 手动配置
编辑 `plugins/OneBot/config.yml`：

```yaml
# go-cqhttp示例
onebot:
  url: "ws://localhost:5700"
  token: ""
  
# Mirai示例  
onebot:
  url: "ws://localhost:8080"
  token: "your-verify-key"
  bot_id: 你的QQ号
  mirai: true
```

## 🧪 测试连接

### 1. 启动Bot框架
确保你的QQ机器人框架正在运行并监听WebSocket连接。

### 2. 启动插件连接
```
/onebot connect
```

### 3. 检查连接状态
```
/onebot status
```

应该看到类似输出：
```
连接状态: 已连接
完整URL: ws://localhost:5700
是否已连接: 是
```

### 4. 发送测试消息
```
# 测试私聊
/onebot test private 你的QQ号 Hello

# 测试群聊  
/onebot test group 测试群号 Hello Group
```

## ❗ 常见安装问题

### 问题1: 插件加载失败
**错误**: `Could not load plugin OneBot`

**解决**:
- 检查Java版本是否为8+
- 检查服务器是否为Bukkit系列
- 确认jar文件完整下载

### 问题2: 依赖冲突
**错误**: `ClassNotFoundException` 或 `NoClassDefFoundError`

**解决**:
- 更新服务器到最新版本
- 检查其他插件是否有冲突
- 尝试使用独立版本的插件

### 问题3: 配置文件错误
**错误**: 连接失败或配置不生效

**解决**:
- 检查YAML格式是否正确
- 确认缩进使用空格而非制表符
- 使用YAML验证工具检查语法

### 问题4: 权限问题
**错误**: 无法使用命令

**解决**:
- 确认有管理员权限或相应插件权限
- 检查服务器权限系统配置

## 🔄 更新插件

### 自动更新检查
插件会在启动时检查更新（如果启用）。

### 手动更新步骤
1. 备份配置文件 `plugins/OneBot/config.yml`
2. 停止服务器
3. 替换插件jar文件
4. 启动服务器
5. 检查配置文件，必要时重新配置

### 版本兼容性
- 配置文件格式可能在大版本间有变化
- 建议查看更新日志了解重大变更
- 测试环境验证后再应用到生产环境

## 📋 安装检查清单

完成安装后，请检查以下项目：

- [ ] 插件成功加载
- [ ] 配置文件生成
- [ ] Bot框架正常运行
- [ ] WebSocket连接成功
- [ ] 基础命令可用
- [ ] 测试消息发送成功
- [ ] 事件接收正常

## 🆘 获取帮助

如果在安装过程中遇到问题：

1. **查看日志**: 检查服务器控制台输出
2. **启用调试**: 在配置中设置 `debug.enabled: true`
3. **查看文档**: 参考 [故障排除](Troubleshooting.md)
4. **提交Issue**: 在GitHub上报告问题
5. **社区讨论**: 参与项目讨论区

## 📚 下一步

安装完成后，建议阅读：
- [快速配置指南](Quick-Setup.md)
- [配置文件详解](Configuration-Guide.md) 
- [API参考文档](API-Reference.md)
- [事件处理指南](Events-Guide.md)