package com.snowbud56.bungeewhitelist.commands

import com.snowbud56.bungeewhitelist.BungeeWhitelist.Companion.instance
import com.snowbud56.bungeewhitelist.commands.Whitelist.messageColor
import com.snowbud56.bungeewhitelist.commands.Whitelist.prefix
import com.snowbud56.bungeewhitelist.commands.Whitelist.valueColor
import com.snowbud56.bungeewhitelist.config.Config
import net.md_5.bungee.api.CommandSender
import com.snowbud56.bungeewhitelist.utils.CommandBase
import com.snowbud56.bungeewhitelist.utils.SubCommand


object Whitelist: CommandBase(
    listOf(
        object: SubCommand (
            "on",
            "Enables the whitelist. [optional server argument]",
            "on [server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                if (args.size == 1) {
                    Config.config.globalEnabled = true
                    sendMessage(sender, "$prefix$messageColor The global whitelist has been enabled!")
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name

                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        Config.config.serverEnabled[server] = true
                        sendMessage(sender, "$prefix$messageColor The whitelist for $valueColor$server$messageColor has been enabled!")
                    }
                }

                return true
            }
        },
        object: SubCommand (
            "off",
            "Disables the whitelist. [optional server argument]",
            "off [server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                if (args.size == 1) {
                    Config.config.globalEnabled = false
                    sendMessage(sender, "$prefix$messageColor The global whitelist has been enabled!")
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name

                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        Config.config.serverEnabled[server] = false
                        sendMessage(sender, "$prefix$messageColor The whitelist for $valueColor$server$messageColor has been enabled!")
                    }
                }

                return true
            }
        },
        object: SubCommand (
            "status",
            "",
            ""
        ) {

        },
        object: SubCommand (
            "add",
            "",
            ""
        ) {

        },
        object: SubCommand (
            "remove",
            "",
            ""
        ) {

        }
    ).associateBy { it.name }.toSortedMap(),
    "bungeewhitelist",
    "bungeewhitelist.use",
    arrayOf("whitelist", "wl")
    ) {
        private val prefix = Config.config.prefix
        private val messageColor = Config.config.messageColor
        private val valueColor = Config.config.valueColor

        override fun onCommand(sender: CommandSender, args: Array<out String>) {
            if (args.isEmpty() || args[0] == "help" || !SubCommands.keys.contains(args[0])) {
                sendMessage(sender, "$prefix$messageColor Whitelist Commands")
                SubCommands.forEach {
                    when {
                        it.value.permissions == null ->
                            sendMessage(sender, "${sender.name} ${it.value.name} ${it.value.parameter} - ${it.value.description}")
                        sender.hasPermission(it.value.permissions) ->
                            sendMessage(sender, "$commandName ${it.value.name} ${it.value.parameter} - ${it.value.description}")
                    }
                }
            } else {
                if (!SubCommands[args[0]]!!.commandExecutor(sender, args))
                    sendMessage(sender, "wrong command")
                sendMessage(sender, "$commandName ${SubCommands[args[0]]!!.name} ${SubCommands[args[0]]!!.parameter}")
            }
        }
    }
}