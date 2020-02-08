package com.snowbud56.bungeewhitelist.commands

import at.pcgamingfreaks.UUIDConverter
import com.snowbud56.bungeewhitelist.BungeeWhitelist.Companion.instance
import com.snowbud56.bungeewhitelist.config.Config
import com.snowbud56.bungeewhitelist.config.Config.messageColor
import com.snowbud56.bungeewhitelist.config.Config.prefix
import com.snowbud56.bungeewhitelist.config.Config.valueColor
import com.snowbud56.bungeewhitelist.config.UUIDCache
import com.snowbud56.bungeewhitelist.config.Whitelist
import net.md_5.bungee.api.CommandSender
import com.snowbud56.bungeewhitelist.utils.CommandBase
import com.snowbud56.bungeewhitelist.utils.SubCommand
import java.util.*


object Whitelist: CommandBase(
    listOf(
        object: SubCommand (
            "on",
            "Enables the whitelist. [optional server argument]",
            "[server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                if (args.size == 1 || args[1] == "") {
                    Config.globalEnabled = true
                    Config.save()
                    sendMessage(sender, "$prefix$messageColor The global whitelist has been enabled!")
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name

                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        Config.setServerStatus(server, true)
                        Config.save()
                        sendMessage(sender, "$prefix$messageColor The whitelist for $valueColor$server$messageColor has been enabled!")
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                if (args.size == 2) {
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
                if (args.size == 1 || args[1] == "") {
                    Config.globalEnabled = false
                    Config.save()
                    sendMessage(sender, "$prefix$messageColor The global whitelist has been disabled!")
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name

                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        Config.setServerStatus(server, false)
                        Config.save()
                        sendMessage(sender, "$prefix$messageColor The whitelist for $valueColor$server$messageColor has been disabled!")
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                if (args.size == 2) {
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

                if (args.size == 1 || args[1] == "") {
                    sendMessage(sender, "$prefix$messageColor Whitelist Status:")
                    sendMessage(sender, "$prefix$messageColor Toggled: $valueColor ${Config.globalEnabled}")
                    sendMessage(sender, "$prefix$messageColor Players whitelisted (${Whitelist.getServer("__global__").size}):")
                    Whitelist.getServer("__global__").forEach {
                        sendMessage(sender, "$valueColor- ${UUIDCache.get(it)} (${it})")
                    }
                } else {
                    val server = instance.proxy.getServerInfo(args[1]).name
                    if (server == null) {
                        sendMessage(sender, "$prefix$messageColor That is not a server on the network!")
                    } else {
                        sendMessage(sender, "$prefix$messageColor Whitelist Status for $valueColor$server$messageColor:")
                        sendMessage(sender, "$prefix$messageColor Toggled: $valueColor ${Config.getServerStatus(server)}")
                        sendMessage(sender, "$prefix$messageColor Players whitelisted (${Whitelist.getServer(server).size}):")
                        Whitelist.getServer(server).forEach {
                            sendMessage(sender, "$valueColor- ${UUIDCache.get(it)} (${it})")
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                if (args.size == 2) {
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
                            if (!Whitelist.contains("__global__", uuid)) {
                                Whitelist.add("__global__", uuid, args[1])
                                sendMessage(
                                    sender,
                                    "$prefix$messageColor Added $valueColor${args[1]} ($uuid)$messageColor to the global whitelist!"
                                )
                            } else {
                                sendMessage(sender,"$prefix$valueColor${args[1]} ($uuid)$messageColor is already added user!")
                            }
                        } else {
                            sendMessage(sender,"$prefix$valueColor ${args[1]}$messageColor is not valid user!")
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
                            if (!Whitelist.contains(server, uuid)) {
                                Whitelist.add(server, uuid, args[1])
                                sendMessage(
                                    sender,
                                    "$prefix$messageColor Added $valueColor${args[1]} ($uuid)$messageColor to the $valueColor$server$messageColor whitelist!"
                                )
                            } else {
                                sendMessage(sender,"$prefix$valueColor${args[1]} ($uuid)$messageColor is already added user!")
                            }
                        } else {
                            sendMessage(sender,"$prefix$valueColor ${args[1]}$messageColor is not valid user!")
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                when (args.size) {
                    2 -> {
                        arg = instance.proxy.players.map { it.name }.toMutableList()
                    }
                    3 -> {
                        arg = instance.proxy.servers.values.map { it.name }.toMutableList()
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
                        val uuid = UUIDCache.getFromName(args[1]) ?: if (instance.proxy.config.isOnlineMode) {
                            UUIDConverter.getUUIDFromName(args[1], true, true, false)
                        } else {
                            UUIDConverter.getUUIDFromName(args[1], false, true)
                        }
                        when {
                            uuid == null -> {
                                sendMessage(sender, "$prefix$messageColor That player is not valid player!")
                            }
                            !Whitelist.contains("__global__", uuid) -> {
                                sendMessage(sender, "$prefix$messageColor That player is not on the global whitelist!")
                            }
                            else -> {
                                Whitelist.remove("__global__", uuid)
                                sendMessage(sender, "$prefix$messageColor Removed $valueColor${args[1]} ($uuid)$messageColor to the global whitelist!")
                            }
                        }
                    }
                    else -> {
                        val server = instance.proxy.getServerInfo(args[2]).name
                        if (server == null) {
                            sendMessage(sender, "$prefix$messageColor That isn't a server on the network!")
                        } else {
                            val uuid = UUIDCache.getFromName(args[1]) ?: if (instance.proxy.config.isOnlineMode) {
                                UUIDConverter.getUUIDFromName(args[1], true, true, false)
                            } else {
                                UUIDConverter.getUUIDFromName(args[1], false, true)
                            }
                            when {
                                uuid == null -> {
                                    sendMessage(sender, "$prefix$messageColor That player is not valid player!")
                                }
                                !Whitelist.contains(server, uuid) -> {
                                    sendMessage(sender, "$prefix$messageColor That player is not on the $valueColor$server$messageColor whitelist!")
                                }
                                else -> {
                                    Whitelist.remove(server, uuid)
                                    sendMessage(sender, "$prefix$messageColor Removed $valueColor${args[1]} ($uuid)$messageColor from the $valueColor$server$messageColor whitelist!")
                                }
                            }
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                when (args.size) {
                    2 -> {
                        arg = instance.proxy.players.map { it.name }.toMutableList()
                    }
                    3 -> {
                        arg = instance.proxy.servers.values.map { it.name }.toMutableList()
                    }
                }

                return arg
            }
        },
        object : SubCommand(
            "forceremove",
            "Remove a UUID from the whitelist. [optional server argument]",
            "<UUID> [server]"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                when (args.size) {
                    1 -> {
                        return false
                    }
                    2 -> {
                        val uuid = UUID.fromString(args[1])
                        when {
                            uuid == null -> {
                                sendMessage(sender, "$prefix$messageColor That player is not valid player!")
                            }
                            !Whitelist.contains("__global__", uuid.toString()) -> {
                                sendMessage(sender, "$prefix$messageColor That player is not on the global whitelist!")
                            }
                            else -> {
                                Whitelist.remove("__global__", uuid.toString())
                                sendMessage(sender, "$prefix$messageColor Removed $valueColor$uuid$messageColor to the global whitelist!")
                            }
                        }
                    }
                    else -> {
                        val server = instance.proxy.getServerInfo(args[2]).name
                        if (server == null) {
                            sendMessage(sender, "$prefix$messageColor That isn't a server on the network!")
                        } else {
                            val uuid = UUID.fromString(args[1])
                            when {
                                uuid == null -> {
                                    sendMessage(sender, "$prefix$messageColor That player is not valid player!")
                                }
                                !Whitelist.contains(server, uuid.toString()) -> {
                                    sendMessage(sender, "$prefix$messageColor That player is not on the $valueColor$server$messageColor whitelist!")
                                }
                                else -> {
                                    Whitelist.remove(server, uuid.toString())
                                    sendMessage(sender, "$prefix$messageColor Removed $valueColor$uuid$messageColor from the $valueColor$server$messageColor whitelist!")
                                }
                            }
                        }
                    }
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                when (args.size) {
                    2 -> {
                        arg = UUIDCache.data.keys.toMutableList()
                    }
                    3 -> {
                        arg = instance.proxy.servers.values.map { it.name }.toMutableList()
                    }
                }

                return arg
            }
        },
        object : SubCommand(
            "removecache",
            "Remove a player's UUID and Nickname from the UUID Cache.",
            "<player>"
        ) {
            override fun commandExecutor(sender: CommandSender, args: Array<out String>): Boolean {
                val uuid = UUIDCache.getFromName(args[1])
                if (uuid != null) {
                    UUIDCache.remove(uuid)
                    sendMessage(sender, "$prefix$messageColor Removed $valueColor$uuid$messageColor from the UUID Cache!")
                } else {
                    sendMessage(sender, "$prefix$messageColor That player is not on the UUID Cache!")
                }

                return true
            }

            override fun tabCompleter(sender: CommandSender, args: Array<out String>): MutableList<String> {
                var arg = mutableListOf<String>()

                when (args.size) {
                    1 -> {
                        arg = UUIDCache.getNames().toMutableList()
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