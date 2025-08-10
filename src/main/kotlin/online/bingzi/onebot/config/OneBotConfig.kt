package online.bingzi.onebot.config

import taboolib.common.platform.function.console
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import java.net.URLEncoder

object OneBotConfig {
    
    @Config("config.yml", autoReload = true)
    lateinit var conf: Configuration
    
    @ConfigNode("onebot.url")
    var url: String = "ws://localhost:8080"
    
    @ConfigNode("onebot.token")
    var token: String = ""
    
    @ConfigNode("onebot.bot_id")
    var botId: Long = 0L
    
    @ConfigNode("onebot.mirai")
    var isMirai: Boolean = false
    
    @ConfigNode("onebot.custom_path")
    var customPath: String = ""
    
    @ConfigNode("onebot.reconnect_interval")
    var reconnectInterval: Int = 5
    
    @ConfigNode("onebot.max_reconnect_attempts")
    var maxReconnectAttempts: Int = -1
    
    @ConfigNode("onebot.connect_timeout")
    var connectTimeout: Int = 10000
    
    @ConfigNode("events.enabled")
    var eventsEnabled: Boolean = true
    
    @ConfigNode("events.thread_pool_size")
    var threadPoolSize: Int = 4
    
    @ConfigNode("debug.enabled")
    var debugEnabled: Boolean = false
    
    @ConfigNode("debug.log_raw_messages")
    var logRawMessages: Boolean = false
    
    @ConfigNode("debug.log_actions")
    var logActions: Boolean = false
    
    fun reload() {
        console().sendMessage("§a[OneBot] 配置文件已重新加载")
    }
    
    /**
     * 获取完整的 WebSocket URL
     */
    fun getWebSocketUrl(): String {
        var finalUrl = url
        
        // 添加自定义路径或默认路径
        val path = when {
            customPath.isNotEmpty() -> customPath
            isMirai -> "/all"
            else -> ""
        }
        
        if (path.isNotEmpty()) {
            finalUrl = if (url.endsWith("/")) {
                url + path.removePrefix("/")
            } else {
                "$url$path"
            }
        }
        
        // 添加参数
        val params = mutableListOf<String>()
        
        // 添加额外参数
        val extraParams = conf.getConfigurationSection("onebot.extra_params")
        extraParams?.getKeys(false)?.forEach { key ->
            val value = extraParams.getString(key)
            if (value != null) {
                params.add("$key=${URLEncoder.encode(value, "UTF-8")}")
            }
        }
        
        // 添加 Mirai 特有参数
        if (isMirai) {
            if (token.isNotEmpty()) {
                params.add("verifyKey=${URLEncoder.encode(token, "UTF-8")}")
            }
            if (botId > 0) {
                params.add("qq=$botId")
            }
        }
        
        // 构建最终URL
        return if (params.isNotEmpty()) {
            "$finalUrl?${params.joinToString("&")}"
        } else {
            finalUrl
        }
    }
    
    /**
     * 获取自定义请求头
     */
    fun getCustomHeaders(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        
        // 添加认证头（如果有token但不是Mirai模式）
        if (token.isNotEmpty() && !isMirai) {
            headers["Authorization"] = "Bearer $token"
        }
        
        // 添加用户自定义头
        val customHeaders = conf.getConfigurationSection("onebot.headers")
        customHeaders?.getKeys(false)?.forEach { key ->
            val value = customHeaders.getString(key)
            if (value != null) {
                headers[key] = value
            }
        }
        
        return headers
    }
    
    /**
     * 应用预设配置
     */
    fun applyPreset(presetName: String): Boolean {
        val preset = conf.getConfigurationSection("presets.$presetName") ?: return false
        
        try {
            preset.getString("url")?.let { url = it }
            preset.getString("path")?.let { customPath = it }
            
            // 应用预设参数
            val presetParams = preset.getConfigurationSection("params")
            if (presetParams != null) {
                val extraParamsSection = conf.createSection("onebot.extra_params")
                presetParams.getKeys(false).forEach { key ->
                    presetParams.getString(key)?.let { value ->
                        extraParamsSection.set(key, value)
                    }
                }
            }
            
            // 应用预设请求头
            val presetHeaders = preset.getConfigurationSection("headers")
            if (presetHeaders != null) {
                val headersSection = conf.createSection("onebot.headers")
                presetHeaders.getKeys(false).forEach { key ->
                    presetHeaders.getString(key)?.let { value ->
                        headersSection.set(key, value)
                    }
                }
            }
            
            console().sendMessage("§a[OneBot] 已应用预设配置: $presetName")
            return true
        } catch (e: Exception) {
            console().sendMessage("§c[OneBot] 应用预设配置失败: ${e.message}")
            return false
        }
    }
    
    /**
     * 获取可用的预设列表
     */
    fun getAvailablePresets(): List<String> {
        return conf.getConfigurationSection("presets")?.getKeys(false)?.toList() ?: emptyList()
    }
}