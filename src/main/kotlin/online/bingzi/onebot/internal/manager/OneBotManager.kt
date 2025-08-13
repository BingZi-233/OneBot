package online.bingzi.onebot.internal.manager

import online.bingzi.onebot.internal.action.ActionFactory
import online.bingzi.onebot.api.OneBotAPI
import online.bingzi.onebot.internal.client.OneBotWebSocketClient
import online.bingzi.onebot.internal.config.OneBotConfig
import online.bingzi.onebot.internal.event.EventFactory
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendError
import taboolib.module.lang.sendInfo
import taboolib.module.lang.sendWarn

/**
 * OneBot 连接管理器
 * 负责管理 WebSocket 连接和各个组件的生命周期
 * 
 * 这是内部管理组件，负责协调各个内部模块的工作
 */
object OneBotManager {

    private var client: OneBotWebSocketClient? = null
    private var eventFactory: EventFactory? = null
    private var actionFactory: ActionFactory? = null

    /**
     * 启动 OneBot 连接
     */
    fun start() {
        if (client?.isOpen == true) {
            console().sendWarn("connection-exists")
            return
        }

        try {
            console().sendInfo("connection-starting")

            // 初始化组件
            eventFactory = EventFactory()
            actionFactory = ActionFactory() // 临时的ActionFactory，稍后会设置client
            client = OneBotWebSocketClient(eventFactory!!, actionFactory!!)

            // 设置ActionFactory的client
            actionFactory!!.setClient(client!!)

            // 初始化 API
            OneBotAPI.initialize(actionFactory!!, client!!)

            // 连接 WebSocket
            submit(async = true) {
                try {
                    client!!.connect()
                    console().sendInfo("connection-started")
                } catch (e: Exception) {
                    console().sendError("connection-failed", e.message ?: "Unknown error")
                    if (OneBotConfig.debugEnabled) {
                        e.printStackTrace()
                    }
                }
            }

        } catch (e: Exception) {
            console().sendError("startup-error", e.message ?: "Unknown error")
            if (OneBotConfig.debugEnabled) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 停止 OneBot 连接
     */
    fun stop() {
        try {
            console().sendInfo("connection-stopping")

            client?.shutdown()
            client = null
            eventFactory = null
            actionFactory = null

            console().sendInfo("connection-stopped")

        } catch (e: Exception) {
            console().sendError("stop-error", e.message ?: "Unknown error")
            if (OneBotConfig.debugEnabled) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 重启连接
     */
    fun restart() {
        console().sendInfo("connection-restarting")
        stop()

        submit(delay = 20L) { // 等待1秒后重启
            start()
        }
    }

    /**
     * 获取连接状态
     */
    fun getStatus(): String {
        return when {
            client == null -> console().asLangText("status-uninitialized")
            client!!.isOpen -> console().asLangText("status-connected")
            else -> console().asLangText("status-disconnected")
        }
    }

    /**
     * 获取当前连接的 URL
     */
    fun getCurrentUrl(): String {
        return OneBotConfig.getWebSocketUrl()
    }
}