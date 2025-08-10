package online.bingzi.onebot

import online.bingzi.onebot.config.OneBotConfig
import online.bingzi.onebot.manager.OneBotManager
import taboolib.common.platform.Platform
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendInfo
import taboolib.module.lang.sendWarn
import taboolib.module.metrics.Metrics
import taboolib.platform.util.bukkitPlugin


object OneBot : Plugin() {

    override fun onEnable() {
        console().sendMessage(console().asLangText("plugin-banner", pluginVersion))

        // 加载配置
        console().sendInfo("loading-config")

        // 检查配置
        if (OneBotConfig.url.isEmpty()) {
            console().sendWarn("warning-no-url")
            return
        }

        // 启动连接（如果配置了地址）
        if (OneBotConfig.eventsEnabled) {
            console().sendInfo("starting-connection")
            OneBotManager.start()
        } else {
            console().sendInfo("events-disabled")
        }

        // 初始化Metrics以收集插件的使用统计信息
        Metrics(26854, bukkitPlugin.description.version, Platform.BUKKIT)

        console().sendInfo("plugin-started")
    }

    override fun onDisable() {
        console().sendInfo("plugin-stopping")

        // 关闭连接
        OneBotManager.stop()

        console().sendInfo("plugin-stopped")
    }
}