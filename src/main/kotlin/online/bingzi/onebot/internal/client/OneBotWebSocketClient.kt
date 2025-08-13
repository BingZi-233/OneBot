package online.bingzi.onebot.internal.client

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import online.bingzi.onebot.internal.action.ActionFactory
import online.bingzi.onebot.internal.config.OneBotConfig
import online.bingzi.onebot.internal.event.EventFactory
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendError
import taboolib.module.lang.sendInfo
import taboolib.module.lang.sendWarn
import java.net.URI

/**
 * OneBot WebSocket 客户端
 * 
 * 内部WebSocket客户端实现，负责与OneBot协议的具体通信
 */
class OneBotWebSocketClient(
    private val eventFactory: EventFactory,
    private val actionFactory: ActionFactory
) : WebSocketClient(URI.create(OneBotConfig.getWebSocketUrl())) {

    private var reconnectAttempts = 0
    private var isManualClose = false

    init {
        // 添加自定义请求头
        val customHeaders = OneBotConfig.getCustomHeaders()
        customHeaders.forEach { (key, value) ->
            addHeader(key, value)
        }

        // 设置连接超时
        connectionLostTimeout = OneBotConfig.connectTimeout
    }

    override fun onOpen(handshake: ServerHandshake) {
        console().sendInfo("connection-established")
        reconnectAttempts = 0

        if (OneBotConfig.debugEnabled) {
            console().sendWarn("handshakeInfo", handshake.httpStatusMessage)
        }
    }

    override fun onMessage(message: String) {
        if (OneBotConfig.logRawMessages) {
            console().sendWarn("receivedMessage", message)
        }

        try {
            val jsonObject = JsonParser.parseString(message).asJsonObject

            // 在异步线程中处理消息，避免阻塞WebSocket线程
            submit(async = true) {
                try {
                    // 检查是否是API响应
                    if (jsonObject.has("echo")) {
                        // 这是API调用的响应，交给ActionFactory处理
                        actionFactory.handleResponse(jsonObject)
                    } else {
                        // 这是事件消息，交给EventFactory处理
                        eventFactory.handleMessage(jsonObject)
                    }
                } catch (e: Exception) {
                    console().sendError("message-processing-error", e.message ?: "Unknown error")
                    if (OneBotConfig.debugEnabled) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            console().sendError("message-parsing-error", e.message ?: "Unknown error")
            if (OneBotConfig.debugEnabled) {
                e.printStackTrace()
            }
        }
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        console().sendWarn("websocket-closed", code.toString(), reason)

        if (!isManualClose && OneBotConfig.maxReconnectAttempts != 0) {
            attemptReconnect()
        }
    }

    override fun onError(ex: Exception) {
        console().sendError("websocket-error", ex.message ?: "Unknown error")
        if (OneBotConfig.debugEnabled) {
            ex.printStackTrace()
        }
    }

    /**
     * 发送动作请求
     */
    fun sendAction(action: String, params: JsonObject? = null, echo: String? = null): Boolean {
        if (!isOpen) {
            console().sendWarn("websocket-not-connected")
            return false
        }

        val request = JsonObject().apply {
            addProperty("action", action)
            if (params != null) {
                add("params", params)
            }
            if (echo != null) {
                addProperty("echo", echo)
            }
        }

        if (OneBotConfig.logActions) {
            console().sendWarn("sendingAction", request.toString())
        }

        try {
            send(request.toString())
            return true
        } catch (e: Exception) {
            console().sendError("action-send-error", e.message ?: "Unknown error")
            return false
        }
    }

    /**
     * 手动关闭连接
     */
    fun shutdown() {
        isManualClose = true
        close()
    }

    /**
     * 尝试重连
     */
    private fun attemptReconnect() {
        if (OneBotConfig.maxReconnectAttempts > 0 && reconnectAttempts >= OneBotConfig.maxReconnectAttempts) {
            console().sendWarn("max-reconnect-attempts-reached")
            return
        }

        reconnectAttempts++
        console().sendWarn("attempting-reconnect", reconnectAttempts.toString(), OneBotConfig.maxReconnectAttempts.toString())

        submit(delay = (OneBotConfig.reconnectInterval * 20L)) {
            try {
                reconnect()
            } catch (e: Exception) {
                console().sendError("reconnect-failed", e.message ?: "Unknown error")
                attemptReconnect()
            }
        }
    }
}