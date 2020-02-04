package com.snowbud56.bungeewhitelist

import com.snowbud56.bungeewhitelist.config.Config
import net.md_5.bungee.api.connection.PendingConnection
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object EventListener : Listener {
    @EventHandler
    fun onServerJoin(e: ServerConnectEvent) {
        val p = e.player
        val server = e.target.name
        if (Config.config.globalEnabled && !WhitelistCommand.whitelisted!!["global"]!!.contains(p.getName())) {
            if (WhitelistCommand.enabled!![server]) {
                if (!WhitelistCommand.whitelisted!![server]!!.contains(p.getName())) {
                    p.sendMessage(ChatUtils.format("&cKicked whilst connecting to $server: ${WhitelistCommand.kickmessage}"))
                    e.setCancelled(true)
                }
            }
        }
    }

    @EventHandler
    fun onNetworkJoin(e: LoginEvent) {
        val p: PendingConnection = e.getConnection()
        if (WhitelistCommand.enabled!!["global"] && !WhitelistCommand.whitelisted!!["global"]!!.contains(p.getName()) || WhitelistCommand.enabled!![p.getListener().getDefaultServer()] && !WhitelistCommand.whitelisted!![p.getListener().getDefaultServer()]!!.contains(p.getName())) {
            p.disconnect(ChatUtils.format("&cKicked whilst connecting to " + p.getListener().getDefaultServer().toString() + ": " + WhitelistCommand.kickmessage))
        }
    }
}