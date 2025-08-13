# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

OneBot 是一个基于 TabooLib 框架开发的 Minecraft Bukkit 插件，用于连接 OneBot 协议。项目使用 Kotlin 语言编写，采用 TabooLib 提供的平台抽象层，支持与各种 QQ 机器人框架的集成。

## 构建命令

### 构建发行版本
发行版本用于正常使用，不含 TabooLib 本体：
```bash
./gradlew build
```

### 构建开发版本
开发版本包含 TabooLib 本体，用于开发者使用，但不可运行：
```bash
./gradlew taboolibBuildApi -PDeleteCode
```

参数 `-PDeleteCode` 表示移除所有逻辑代码以减少体积。

### 发布到仓库
```bash
./gradlew publish
```
需要设置 `MAVEN_USERNAME` 和 `MAVEN_PASSWORD` 环境变量。

## 核心架构

### 包结构设计

项目采用清晰的包结构划分，分为对外API和内部实现：

#### api 包 - 对外接口和事件
- `api/` - 公共API接口，其他插件可直接调用
  - `OneBotAPI.kt` - 主要的公共API接口，提供异步回调方式的方法
- `api/event/` - 供其他插件监听的事件系统
  - `OneBotEvent.kt` - 所有OneBot事件的基类
  - `message/` - 消息相关事件（私聊、群聊消息）
  - `notice/` - 通知相关事件（群成员变动、好友添加等）
  - `request/` - 请求相关事件（好友申请、入群申请等）

#### internal 包 - 内部实现
- `internal/manager/` - `OneBotManager` 连接生命周期管理
- `internal/client/` - `OneBotWebSocketClient` WebSocket客户端实现  
- `internal/action/` - `ActionFactory` API调用处理和封装
- `internal/event/` - `EventFactory` 事件工厂和处理逻辑
- `internal/config/` - `OneBotConfig` 配置管理和预设处理
- `internal/command/` - `OneBotCommand` 插件管理命令

### 主要组件和职责

1. **OneBotManager** (internal) - 连接生命周期管理
   - 启动和停止 WebSocket 连接
   - 管理重连机制
   - 协调各组件初始化

2. **OneBotWebSocketClient** (internal) - WebSocket 通信
   - 实现 OneBot 协议的 WebSocket 客户端
   - 处理消息收发和连接状态
   - 自动重连和错误处理

3. **EventFactory** (internal) - 事件分发系统
   - 解析 OneBot 事件并转换为 API 事件
   - 创建api包下的事件实例供其他插件监听

4. **ActionFactory** (internal) - API 调用处理
   - 封装 OneBot API 的异步调用
   - 基于 CompletableFuture 提供异步操作接口
   - 使用 handle() 方法避免阻塞操作

5. **OneBotAPI** (api) - 公共接口层
   - 为其他插件提供简洁的异步API
   - 所有方法均为回调模式，避免阻塞主线程
   - 确保所有网络操作在后台执行

6. **OneBotConfig** (internal) - 配置管理
   - 支持多种机器人框架的预设配置
   - 动态 URL 构建和请求头管理
   - 配置热重载支持

### 包结构说明

```
online.bingzi.onebot
├── OneBot.kt                         # 插件主类，处理启用/禁用逻辑
├── api/                              # 对外API包，其他插件使用的接口
│   ├── OneBotAPI.kt                  # 主要的公共API接口
│   └── event/                        # 事件系统，供其他插件监听
│       ├── OneBotEvent.kt            # 事件基类
│       ├── message/                  # 消息事件包
│       │   ├── MessageEvent.kt       # 消息事件基类
│       │   ├── PrivateMessageEvent.kt # 私聊消息事件
│       │   └── GroupMessageEvent.kt  # 群消息事件
│       ├── notice/                   # 通知事件包
│       │   ├── NoticeEvent.kt        # 通知事件基类
│       │   ├── GroupIncreaseNotice.kt # 群成员增加通知
│       │   ├── GroupDecreaseNotice.kt # 群成员减少通知
│       │   ├── GroupBanNotice.kt     # 群禁言通知
│       │   └── FriendAddNotice.kt    # 好友添加通知
│       └── request/                  # 请求事件包
│           ├── RequestEvent.kt       # 请求事件基类
│           ├── FriendRequestEvent.kt # 好友请求事件
│           └── GroupRequestEvent.kt  # 群请求事件
└── internal/                         # 内部实现包，不供外部直接使用
    ├── manager/OneBotManager.kt      # 连接管理器，组件生命周期控制
    ├── client/OneBotWebSocketClient.kt # WebSocket 客户端实现
    ├── action/ActionFactory.kt      # API 调用工厂，异步操作封装
    ├── event/EventFactory.kt        # 事件工厂，解析和分发事件
    ├── config/OneBotConfig.kt       # 配置文件管理，预设配置系统
    └── command/OneBotCommand.kt     # 管理命令实现
```

### TabooLib 模块配置

项目在 `build.gradle.kts` 中配置了以下 TabooLib 模块：
- **Bukkit** + **BukkitUtil**: Bukkit 平台支持和工具集
- **Basic**: 基础工具，包括配置文件处理
- **I18n**: 国际化支持，处理多语言文件
- **Metrics**: 收集插件使用数据并发送到 bStats (ID: 26854)
- **MinecraftChat**: 处理聊天组件，支持富文本消息

### 依赖管理和重定位

项目使用以下运行时依赖（通过 `taboo()` 方式）：
- **org.java-websocket:Java-WebSocket:1.5.4** - WebSocket 客户端
- **com.google.code.gson:gson:2.10.1** - JSON 处理
- **com.squareup.okhttp3:okhttp:4.12.0** - HTTP 客户端

这些依赖会被重定位到 `online.bingzi.onebot.rely` 包下以避免冲突。

### 国际化系统

- 语言文件位于 `src/main/resources/lang/`
- 支持中文 (`zh_CN.yml`) 和英文 (`en_US.yml`)
- 使用扁平化 YAML 结构，不接受嵌套
- 通过 TabooLib 的 `sendInfo/sendWarn/sendError` 进行消息发送

### 事件系统设计

1. **事件继承结构**:
   - `OneBotEvent` (基类) → 具体事件类型的基类 → 具体事件实现
   - 所有事件都可通过 `SubscribeEvent` 监听

2. **事件包结构**:
   - **message**: 消息事件包
     - `MessageEvent` - 消息事件基类
     - `PrivateMessageEvent` - 私聊消息事件
     - `GroupMessageEvent` - 群消息事件
   - **notice**: 通知事件包
     - `NoticeEvent` - 通知事件基类
     - `GroupIncreaseNotice` - 群成员增加通知
     - `GroupDecreaseNotice` - 群成员减少通知
     - `GroupBanNotice` - 群禁言通知
     - `FriendAddNotice` - 好友添加通知
   - **request**: 请求事件包
     - `RequestEvent` - 请求事件基类
     - `FriendRequestEvent` - 好友请求事件
     - `GroupRequestEvent` - 群请求事件

3. **事件处理机制**:
   - EventFactory 负责解析 JSON 并创建事件对象
   - 根据事件类型路由到对应的事件包
   - 通过 TabooLib 事件系统分发到各个监听器
   - 支持事件响应方法（reply、replyWithAt等）

### 配置系统特性

- **预设配置**: 支持 go-cqhttp、Mirai、NoneBot2 等主流框架
- **动态 URL 构建**: 自动处理路径、参数和认证
- **多种认证方式**: Bearer Token、Mirai verifyKey 等
- **配置热重载**: 支持运行时重载配置文件

## 开发约定

### 代码组织规范
- **内部模块放在 `internal` 包下** - 所有内部实现和具体逻辑
- **对外 API 放在 `api` 包下** - 公共接口和事件供其他插件使用
- **每个功能模块有独立的包名** - 便于维护和扩展
- **严格的包边界** - internal包内的类不应被外部直接使用

### 国际化规范
- 语言文件使用小驼峰命名
- 不对消息文本进行硬编码
- 优先使用 TabooLib 的国际化消息发送方法

### 错误处理模式
- 使用 TabooLib 的日志系统
- 调试模式下打印详细错误栈
- 通过配置控制日志详细程度

### 异步编程规范
- **严禁使用同步方法** - 所有API调用必须使用异步回调模式
- **避免阻塞主线程** - 使用 CompletableFuture.handle() 而不是 .get()
- **回调在主线程执行** - 通过 submit(async = false) 确保回调在主线程
- **错误处理** - 在回调中妥善处理异常情况

## 测试和调试

### 内置命令
```bash
/onebot status          # 查看连接状态和配置信息
/onebot connect         # 手动连接
/onebot restart         # 重启连接
/onebot test private <QQ号> [消息]  # 测试私聊
/onebot test group <群号> [消息]    # 测试群消息
```

### 调试配置
在 `config.yml` 中启用调试模式：
```yaml
debug:
  enabled: true
  log_raw_messages: true    # 记录原始消息
  log_actions: true         # 记录 API 调用
```