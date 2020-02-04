package com.snowbud56.bungeewhitelist

import com.snowbud56.bungeewhitelist.BungeeWhitelist.Companion.instance
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.util.*

class WhitelistCommand : Command("bungeewhitelist", "bungeewhitelist.use"), Listener {
    override fun execute(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Whitelist Commands"))
            sender.sendMessage(ChatUtils.format("$prefix$messagecolor/bungeewhitelist <on | off> [server]: Enables/disables the whitelist. [optional server argument]"))
            sender.sendMessage(ChatUtils.format("$prefix$messagecolor/bungeewhitelist status [server]: Views the status of the whitelist. [optional server argument]"))
            sender.sendMessage(ChatUtils.format("$prefix$messagecolor/bungeewhitelist add <player> [server]: Adds a player to the whitelist. [optional server argument]"))
            sender.sendMessage(ChatUtils.format("$prefix$messagecolor/bungeewhitelist remove <player> [server]: Removes a player from the whitelist. [optional server argument]"))
        } else {
            if (args[0].equals("on", ignoreCase = true) || args[0].equals("enable", ignoreCase = true)) {
                if (args.size == 1) {
                    enabled!!["global"] = true
                    sender.sendMessage(ChatUtils.format(prefix + messagecolor + "The global whitelist has been enabled!"))
                } else {
                    val server: String = instance.getProxy().getConfig().getServers().get(args[1]).getName()
                    if (server == null) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That is not a server on the network!")) else {
                        if (enabled!![server]) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That server's whitelist is already enabled!")) else {
                            enabled!![server] = true
                            sender.sendMessage(ChatUtils.format(prefix + messagecolor + "The whitelist for " + valuecolor + server + messagecolor + " has been enabled!"))
                        }
                    }
                }
            } else if (args[0].equals("off", ignoreCase = true) || args[0].equals("disable", ignoreCase = true)) {
                if (args.size == 1) {
                    enabled!!["global"] = false
                    sender.sendMessage(ChatUtils.format(prefix + messagecolor + "The global whitelist has been disabled!"))
                } else {
                    val server: String = instance.getProxy().getConfig().getServers().get(args[1]).getName()
                    if (server == null) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That is not a server on the network!")) else {
                        if (!enabled!![server]!!) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That server's whitelist is already disabled!")) else {
                            enabled!![server] = false
                            sender.sendMessage(ChatUtils.format(prefix + messagecolor + "The whitelist for " + valuecolor + server + messagecolor + " has been disabled!"))
                        }
                    }
                }
            } else if (args[0].equals("status", ignoreCase = true) || args[0].equals("list", ignoreCase = true)) {
                if (args.size == 1) {
                    sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Whitelist Status:"))
                    sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Toggled: " + valuecolor + enabled!!["global"]))
                    sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Players whitelisted:"))
                    for (player in whitelisted!!["global"]!!) sender.sendMessage(ChatUtils.format("$prefix$valuecolor- $player"))
                } else {
                    val server: String = instance.getProxy().getConfig().getServers().get(args[1]).getName()
                    if (server == null) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That is not a server on the network!")) else {
                        sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Whitelist Status for " + valuecolor + server + messagecolor + ":"))
                        sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Toggled: " + valuecolor + enabled!![server]))
                        sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Players whitelisted:"))
                        for (player in whitelisted!![server]!!) sender.sendMessage(ChatUtils.format("$prefix$valuecolor- $player"))
                    }
                }
            } else if (args[0].equals("add", ignoreCase = true)) {
                if (args.size == 1) sender.sendMessage(ChatUtils.format(prefix + "Usage: /bungeewhitelist add <player>")) else if (args.size == 2) {
                    whitelisted!!["global"].add(args[1])
                    sender.sendMessage(ChatUtils.format(prefix + "Added " + valuecolor + args[1] + messagecolor + " to the global whitelist!"))
                } else {
                    val server: String = BungeeWhitelist.Companion.getInstance().getProxy().getServerInfo(args[2]).getName()
                    whitelisted!![server].add(args[1])
                    sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Added " + valuecolor + args[1] + messagecolor + " to the " + valuecolor + server + messagecolor + " whitelist!"))
                }
            } else if (args[0].equals("remove", ignoreCase = true)) {
                if (args.size == 1) sender.sendMessage(ChatUtils.format(prefix + "Usage: /bungeewhitelist remove <player>")) else if (args.size == 2) {
                    if (!whitelisted!!["global"]!!.contains(args[1])) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That player is not on the global whitelist!")) else {
                        whitelisted!!["global"].remove(args[1])
                        sender.sendMessage(ChatUtils.format(prefix + "Added " + valuecolor + args[1] + messagecolor + " to the global whitelist!"))
                    }
                } else {
                    val server: String = BungeeWhitelist.Companion.getInstance().getProxy().getServerInfo(args[2]).getName()
                    if (server == null) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That isn't a server on the network!")) else {
                        if (!whitelisted!!["global"]!!.contains(args[1])) sender.sendMessage(ChatUtils.format(prefix + messagecolor + "That player is not on the global whitelist!")) else {
                            whitelisted!![server].remove(args[1])
                            sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Removed " + valuecolor + args[1] + messagecolor + " from the " + valuecolor + server + messagecolor + " whitelist!"))
                        }
                    }
                }
            } else sender.sendMessage(ChatUtils.format(prefix + messagecolor + "Invalid usage! Type \"/bungeewhitelist\" for a list of commands"))
        }
    }

    companion object {

        fun pluginEnable() {
            enabled = HashMap()
            whitelisted = HashMap()
            try {
                val whitelistconfig: Configuration = YamlConfiguration.getProvider(YamlConfiguration::class.java).load(File(BungeeWhitelist.Companion.getInstance().getDataFolder().toString() + "/config.yml"))
                for (servername in BungeeWhitelist.Companion.getInstance().getProxy().getConfig().getServers().keySet()) { //BungeeEssentials.getInstance().getLogger().info(servername);
                    enabled[servername] = whitelistconfig.getBoolean("whitelist.$servername.enabled")
                    whitelisted[servername] = whitelistconfig.getStringList("whitelist.$servername.whitelisted")
                }
                enabled["global"] = whitelistconfig.getBoolean("whitelist.global.enabled")
                whitelisted["global"] = whitelistconfig.getStringList("whitelist.global.whitelisted")
                prefix = whitelistconfig.getString("config.prefix")
                valuecolor = whitelistconfig.getString("config.value-color")
                messagecolor = whitelistconfig.getString("config.message-color")
                kickmessage = whitelistconfig.getString("config.kick-message")
            } catch (e: Exception) {
                BungeeWhitelist.Companion.getInstance().getLogger().severe("Failed to get config! Whitelist won't work without it!")
                e.printStackTrace()
            }
        }
    }
}