package com.snowbud56.bungeewhitelist.commands

import at.pcgamingfreaks.UUIDConverter
import com.snowbud56.bungeewhitelist.BungeeWhitelist.Companion.instance
import com.snowbud56.bungeewhitelist.commands.Whitelist.messageColor
import com.snowbud56.bungeewhitelist.commands.Whitelist.prefix
import com.snowbud56.bungeewhitelist.commands.Whitelist.valueColor
import com.snowbud56.bungeewhitelist.config.Config
import com.snowbud56.bungeewhitelist.config.Whitelist
import net.md_5.bungee.api.CommandSender
import com.snowbud56.bungeewhitelist.utils.CommandBase
import com.snowbud56.bungeewhitelist.utils.SubCommand


object Whitelist: CommandBase(
    listOf(
        object: SubCommand (
            "on",
            "Enables the whitelist. [optional server argument]",
            "[server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                if (args.size == 1) {
                    Config.config.globalEnabled = true
                    Config.save()
                    sendMessage(sender, "$prefix$messageColor The global whitelist has been enabled!")
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name

                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        Config.config.serverEnabled[server] = true
                        Config.save()
                        sendMessage(sender, "$prefix$messageColor The whitelist for $valueColor$server$messageColor has been enabled!")
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                if (args.size == 1) {
                    arg = instance.proxy.servers.keys.toMutableList()
                    arg.add("")
                }

                return arg
            }
        },
        object: SubCommand (
            "off",
            "Disables the whitelist. [optional server argument]",
            "[server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                if (args.size == 1) {
                    Config.config.globalEnabled = false
                    Config.save()
                    sendMessage(sender, "$prefix$messageColor The global whitelist has been disabled!")
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name

                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        Config.config.serverEnabled[server] = false
                        Config.save()
                        sendMessage(sender, "$prefix$messageColor The whitelist for $valueColor$server$messageColor has been disabled!")
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                if (args.size == 1) {
                    arg = instance.proxy.servers.map { it.value.name }.toMutableList()
                    arg.add("")
                }

                return arg
            }
        },
        object: SubCommand (
            "list",
            "Views the status of the whitelist. [optional server argument]",
            "[server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {

                if (args.size == 1) {
                    sendMessage(sender, "$prefix$messageColor Whitelist Status:")
                    sendMessage(sender, "$prefix$messageColor Toggled: $valueColor ${Config.config.globalEnabled}")
                    sendMessage(sender, "$prefix$messageColor Players whitelisted (${Whitelist.config["__global__"]?.size ?: 0}):")
                    Whitelist.config["__global__"]?.forEach {
                        sendMessage(sender, "$valueColor- ${UUIDConverter.getNameFromUUID(it)} ($it)")
                    }
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name
                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        sendMessage(sender, "$prefix$messageColor Whitelist Status for $valueColor$server$messageColor:")
                        sendMessage(sender, "$prefix$messageColor Toggled: $valueColor ${Config.config.serverEnabled[server]?: false}")
                        sendMessage(sender, "$prefix$messageColor Players whitelisted (${Whitelist.config[server]?.size ?: 0}):")
                        Whitelist.config[server]?.forEach {
                            sendMessage(sender, "$valueColor- ${UUIDConverter.getNameFromUUID(it)} ($it)")
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                if (args.size == 1) {
                    arg = instance.proxy.servers.map { it.value.name }.toMutableList()
                    arg.add("")
                }

                return arg
            }
        },
        object: SubCommand (
            "add",
            "Adds a player to the whitelist. [optional server argument]",
            "<player> [server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                when (args.size) {
                    1 -> {
                        return false
                    }
                    2 -> {
                        val uuid = if (instance.proxy.config.isOnlineMode) {
                            UUIDConverter.getUUIDFromName(args[1], true, true, false)
                        } else {
                            UUIDConverter.getUUIDFromName(args[1], false, true)
                        }

                        if (uuid != null) {
                            if (Whitelist.config["__global__"]?.contains(uuid) != true) {
                                Whitelist.config["__global__"]?.let { it.add(uuid) } ?: mutableListOf(uuid)
                                Whitelist.save()
                                sendMessage(
                                    sender,
                                    "$prefix$messageColor Added $valueColor${args[1]} ($uuid)$messageColor to the global whitelist!"
                                )
                            } else {
                                sendMessage(sender,"$prefix$valueColor${args[1]} ($uuid)$messageColor is already added user!")
                            }
                        } else {
                            sendMessage(sender,"$prefix$valueColor${args[1]} $messageColor is not valid user!")
                        }
                    }
                    else -> {
                        val server = instance.proxy.getServerInfo(args[2]).name
                        val uuid = if (instance.proxy.config.isOnlineMode) {
                            UUIDConverter.getUUIDFromName(args[1], true, true, false)
                        } else {
                            UUIDConverter.getUUIDFromName(args[1], false, true)
                        }

                        if (uuid != null) {
                            if (Whitelist.config[server]?.contains(uuid) != true) {
                                Whitelist.config[server]?.let { it.add(uuid) } ?: mutableListOf(uuid)
                                Whitelist.save()
                                sendMessage(
                                    sender,
                                    "$prefix$messageColor Added $valueColor${args[1]} ($uuid)$messageColor to the $valueColor$server$messageColor whitelist!"
                                )
                            } else {
                                sendMessage(sender,"$prefix$valueColor${args[1]} ($uuid)$messageColor is already added user!")
                            }
                        } else {
                            sendMessage(sender,"$prefix$valueColor${args[1]} $messageColor is not valid user!")
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                when (args.size) {
                    1 -> {
                        arg = instance.proxy.players.map { it.name }.toMutableList()
                    }
                    2 -> {
                        arg = instance.proxy.servers.values.map { it.name }.toMutableList()
                        arg.add("")
                    }
                }

                return arg
            }
        },
        object: SubCommand (
            "remove",
            "Removes a player from the whitelist. [optional server argument]",
            "<player> [server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                when (args.size) {
                    1 -> {
                        return false
                    }
                    2 -> {
                        val uuid = UUIDConverter.getUUIDFromName(args[1], true, true, true)
                        if (!Whitelist.config["__global__"]!!.contains(uuid)) {
                            sendMessage(sender, "$prefix$messageColor That player is not on the global whitelist!")
                        } else {
                            Whitelist.config["__global__"]!!.remove(uuid)
                            sendMessage(sender, "$prefix$messageColor Removed $valueColor${args[1]} ($uuid)$messageColor to the global whitelist!")
                        }
                    }
                    else -> {
                        val server = instance.proxy.getServerInfo(args[2]).name
                        if (server == null) {
                            sendMessage(sender, "$prefix$messageColor That isn't a server on the network!")
                        } else {
                            val uuid = UUIDConverter.getUUIDFromName(args[1], true, true, true)
                            if (!Whitelist.config["__global__"]!!.contains(uuid)) {
                                sendMessage(sender, "$prefix$messageColor That player is not on the global whitelist!")
                            } else {
                                Whitelist.config[server]!!.remove(uuid)
                                sendMessage(sender, "$prefix$messageColor Removed $valueColor${args[1]} ($uuid)$messageColor from the $valueColor$server$messageColor whitelist!")
                            }
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                when (args.size) {
                    1 -> {
                        arg = instance.proxy.players.map { it.name }.toMutableList()
                    }
                    2 -> {
                        arg = instance.proxy.servers.values.map { it.name }.toMutableList()
                        arg.add("")
                    }
                }

                return arg
            }
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
                        sendMessage(sender, "/$commandName ${it.value.name} ${it.value.parameter} - ${it.value.description}")
                    sender.hasPermission(it.value.permissions) ->
                        sendMessage(sender, "/$commandName ${it.value.name} ${it.value.parameter} - ${it.value.description}")
                }
            }
        } else {
            if (!SubCommands[args[0]]!!.commandExecutor(sender, args)) {
                sendMessage(sender, "$prefix$messageColor wrong command")
                sendMessage(sender, "$prefix$messageColor /$commandName ${SubCommands[args[0]]!!.name} ${SubCommands[args[0]]!!.parameter}")
            }
        }
    }
}