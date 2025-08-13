package online.bingzi.onebot.internal.event

import online.bingzi.onebot.api.event.status.OneBotConnectedEvent
import online.bingzi.onebot.api.event.status.OneBotDisconnectedEvent
import online.bingzi.onebot.api.event.status.OneBotReconnectingEvent
import online.bingzi.onebot.api.event.status.StatusEvent
import taboolib.common.platform.function.submit

/**
 * 状态事件工厂，用于创建和分发OneBot状态相关的事件
 * 
 * 内部状态事件处理组件，负责创建和分发状态变化事件
 */
class StatusEventFactory {

    /**
     * 触发连接成功事件
     */
    fun fireConnectedEvent(serverUrl: String, selfId: Long = 0L, reconnectCount: Int = 0) {
        val event = OneBotConnectedEvent(
            time = System.currentTimeMillis() / 1000,
            selfId = selfId,
            rawData = createStatusJson("connected", serverUrl, reconnectCount),
            serverUrl = serverUrl,
            reconnectCount = reconnectCount
        )
        fireStatusEvent(event)
    }

    /**
     * 触发连接断开事件
     */
    fun fireDisconnectedEvent(
        serverUrl: String,
        selfId: Long = 0L,
        reason: String,
        isNormalClose: Boolean = false,
        closeCode: Int = -1
    ) {
        val event = OneBotDisconnectedEvent(
            time = System.currentTimeMillis() / 1000,
            selfId = selfId,
            rawData = createStatusJson("disconnected", serverUrl, closeCode, reason),
            serverUrl = serverUrl,
            reason = reason,
            isNormalClose = isNormalClose,
            closeCode = closeCode
        )
        fireStatusEvent(event)
    }

    /**
     * 触发重连事件
     */
    fun fireReconnectingEvent(
        serverUrl: String,
        selfId: Long = 0L,
        attemptCount: Int,
        maxAttempts: Int,
        retryDelay: Long
    ) {
        val event = OneBotReconnectingEvent(
            time = System.currentTimeMillis() / 1000,
            selfId = selfId,
            rawData = createStatusJson("reconnecting", serverUrl, attemptCount, maxAttempts, retryDelay),
            serverUrl = serverUrl,
            attemptCount = attemptCount,
            maxAttempts = maxAttempts,
            retryDelay = retryDelay
        )
        fireStatusEvent(event)
    }

    /**
     * 在主线程中触发状态事件
     */
    private fun fireStatusEvent(event: StatusEvent) {
        submit(async = false) {
            try {
                event.call()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 创建状态事件的JSON数据
     */
    private fun createStatusJson(status: String, serverUrl: String, vararg params: Any): String {
        return "{\"post_type\":\"status\",\"status_type\":\"$status\",\"server_url\":\"$serverUrl\",\"time\":${System.currentTimeMillis() / 1000},\"params\":[${params.joinToString(",")}]}"
    }
}