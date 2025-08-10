# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

OneBot 是一个基于 TabooLib 框架开发的 Minecraft Bukkit 插件，用于连接 OneBot 协议。项目使用 Kotlin 语言编写，采用 TabooLib 提供的平台抽象层。

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

## 架构结构

### TabooLib 模块配置
项目在 `build.gradle.kts` 中配置了以下 TabooLib 模块：
- **Bukkit**: 提供 Bukkit 平台支持和通用工具
- **Basic**: 基础工具，包括配置文件处理
- **I18n**: 国际化支持，处理多语言文件
- **Metrics**: 收集插件使用数据并发送到 bStats
- **MinecraftChat**: 处理聊天组件，支持富文本消息

### 核心类结构
- `OneBot` 对象继承自 `taboolib.common.platform.Plugin`，作为插件主类
- 使用 TabooLib 的平台抽象层进行跨版本兼容

### 技术规范
- Kotlin 2.2.0
- Java 8 兼容性 (JVM_1_8)
- TabooLib 版本: 6.2.3-ee81cb0

## 依赖管理

项目使用以下编译时依赖：
- `ink.ptms.core:v12004` (mapped 和 universal)
- Kotlin 标准库
- 本地 libs 目录下的 JAR 文件

## helpful 目录

项目包含一个 `helpful/onebot-client/` 子目录，这是一个独立的 Java 项目，包含：
- OneBot 客户端实现 (`OneBotClient.java`)
- WebSocket 连接处理 (`WSClient.java`)
- 事件总线和注解系统
- 动作工厂和消息处理器

这个子项目可能是用于测试或作为客户端库的实现参考。