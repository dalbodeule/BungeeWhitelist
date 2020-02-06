package com.snowbud56.bungeewhitelist

import com.snowbud56.bungeewhitelist.config.Config
import com.snowbud56.bungeewhitelist.config.Whitelist
import com.snowbud56.bungeewhitelist.utils.getColored
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.PendingConnection
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

object EventListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    internal fun onServerJoin(e: ServerConnectEvent) {
        val p = e.player
        val server = e.target.name

        if (
            Config.config.serverEnabled[server] == true &&
            Whitelist.config[server]?.contains(p.uniqueId.toString()) == false
        ) {
            p.sendMessage(TextComponent("&cKicked whilst connecting to $server: ${Config.config.kickMessage}".getColored))
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    internal fun onNetworkJoin(e: LoginEvent) {
        val p: PendingConnection = e.connection
        if (
            Config.config.globalEnabled &&
            Whitelist.config["__global__"]?.contains(p.uniqueId.toString()) == false
        ) {
            p.disconnect(
                TextComponent(
                    "&cKicked whilst connecting to BungeeCord:\n${Config.config.kickMessage}".getColored
                )
            )
        }
    }
}