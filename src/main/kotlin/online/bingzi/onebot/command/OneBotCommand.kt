package online.bingzi.onebot.command

import online.bingzi.onebot.api.OneBotAPI
import online.bingzi.onebot.config.OneBotConfig
import online.bingzi.onebot.manager.OneBotManager
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import taboolib.module.lang.sendInfo
import taboolib.module.lang.sendLang

/**
 * OneBot 插件命令
 */
@CommandHeader(
    name = "onebot",
    aliases = ["ob"],
    description = "OneBot 插件管理命令"
)
object OneBotCommand {

    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            showHelp(sender)
        }
    }

    @CommandBody
    val help = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            showHelp(sender)
        }
    }

    @CommandBody
    val status = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            showStatus(sender)
        }
    }

    @CommandBody
    val connect = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-connecting")
            OneBotManager.start()
        }
    }

    @CommandBody
    val disconnect = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-disconnecting")
            OneBotManager.stop()
        }
    }

    @CommandBody
    val restart = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-restarting")
            OneBotManager.restart()
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-reloading")
            OneBotConfig.reload()
            sender.sendLang("command-reloaded")
        }
    }

    @CommandBody
    val preset = subCommand {
        literal("list") {
            execute<ProxyCommandSender> { sender, _, _ ->
                val presets = OneBotConfig.getAvailablePresets()
                sender.sendLang("preset-list")
                if (presets.isEmpty()) {
                    sender.sendMessage(sender.asLangText("preset-none"))
                } else {
                    presets.forEach { preset ->
                        sender.sendMessage("§e  - $preset")
                    }
                }
            }
        }

        literal("apply") {
            dynamic("presetName") {
                suggestion<ProxyCommandSender>(uncheck = true) { _, _ ->
                    OneBotConfig.getAvailablePresets()
                }
                execute<ProxyCommandSender> { sender, context, argument ->
                    val presetName = argument
                    if (OneBotConfig.applyPreset(presetName)) {
                        sender.sendLang("preset-applied", presetName)
                        sender.sendLang("preset-restart-reminder")
                    } else {
                        sender.sendLang("preset-not-found", presetName)
                    }
                }
            }
        }
    }

    @CommandBody
    val test = subCommand {
        literal("private") {
            dynamic("userId") {
                suggestion<ProxyCommandSender>(uncheck = true) { _, _ ->
                    listOf(console().asLangText("input-qq-number"))
                }
                dynamic("message", optional = true) {
                    suggestion<ProxyCommandSender>(uncheck = true) { _, _ ->
                        listOf(console().asLangText("test-message"))
                    }
                    execute<ProxyCommandSender> { sender, context, argument ->
                        val userId = context["userId"].toLongOrNull()
                        val message = argument.ifEmpty { sender.asLangText("test-message") }

                        if (userId == null) {
                            sender.sendLang("invalid-qq")
                            return@execute
                        }

                        // 异步发送消息，避免阻塞主线程
                        OneBotAPI.sendPrivateMessage(userId, message) { success ->
                            if (success) {
                                sender.sendLang("private-message-success")
                            } else {
                                sender.sendLang("private-message-failed")
                            }
                        }
                    }
                }
            }
        }

        literal("group") {
            dynamic("groupId") {
                suggestion<ProxyCommandSender>(uncheck = true) { _, _ ->
                    listOf(console().asLangText("input-group-number"))
                }
                dynamic("message", optional = true) {
                    suggestion<ProxyCommandSender>(uncheck = true) { _, _ ->
                        listOf(console().asLangText("test-message"))
                    }
                    execute<ProxyCommandSender> { sender, context, argument ->
                        val groupId = context["groupId"].toLongOrNull()
                        val message = argument.ifEmpty { sender.asLangText("test-message") }

                        if (groupId == null) {
                            sender.sendLang("invalid-group")
                            return@execute
                        }

                        // 异步发送消息，避免阻塞主线程
                        OneBotAPI.sendGroupMessage(groupId, message) { success ->
                            if (success) {
                                sender.sendLang("group-message-success")
                            } else {
                                sender.sendLang("group-message-failed")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showHelp(sender: ProxyCommandSender) {
        sender.sendMessage(sender.asLangText("command-help"))
    }

    private fun showStatus(sender: ProxyCommandSender) {
        sender.sendMessage(sender.asLangText("status-header"))

        sender.sendLang("status-connection", OneBotManager.getStatus())
        sender.sendLang("status-base-url", OneBotConfig.url)
        sender.sendLang("status-full-url", OneBotManager.getCurrentUrl())
        sender.sendLang("status-is-connected", if (OneBotAPI.isConnected()) sender.asLangText("status-yes") else sender.asLangText("status-no"))
        sender.sendLang("status-events-enabled", if (OneBotConfig.eventsEnabled) sender.asLangText("status-enabled") else sender.asLangText("status-disabled"))
        sender.sendLang("status-debug-mode", if (OneBotConfig.debugEnabled) sender.asLangText("status-enabled") else sender.asLangText("status-disabled"))
        sender.sendLang("status-custom-path", OneBotConfig.customPath.ifEmpty { sender.asLangText("status-no-custom-path") })
        sender.sendLang("status-mirai-mode", if (OneBotConfig.isMirai) sender.asLangText("status-yes") else sender.asLangText("status-no"))

        val customHeaders = OneBotConfig.getCustomHeaders()
        if (customHeaders.isNotEmpty()) {
            sender.sendInfo("status-custom-headers")
            customHeaders.forEach { (key, value) ->
                val displayValue = if (key.lowercase().contains("auth") || key.lowercase().contains("token")) {
                    sender.asLangText("status-header-hidden")
                } else value
                sender.sendMessage("§7   $key: $displayValue")
            }
        }

        sender.sendMessage(sender.asLangText("status-footer"))
    }
}