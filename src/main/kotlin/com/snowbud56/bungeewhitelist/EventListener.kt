package com.snowbud56.bungeewhitelist

import com.snowbud56.bungeewhitelist.config.Config
import com.snowbud56.bungeewhitelist.config.UUIDCache
import com.snowbud56.bungeewhitelist.config.Whitelist
import com.snowbud56.bungeewhitelist.utils.getColored
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.PendingConnection
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import java.util.*

object EventListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    internal fun onServerJoin(e: ServerConnectEvent) {
        val p = e.player
        val server = e.target.name

        if (
            Config.getServerStatus(server) && !Whitelist.contains(server, p.uniqueId.toString())
        ) {
            p.sendMessage(TextComponent("&cKicked whilst connecting to $server: ${Config.kickMessage}".getColored))
            e.isCancelled = true
        } else {
            UUIDCache.addOrRename(p.uniqueId.toString(), p.name)
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    internal fun onNetworkJoin(e: LoginEvent) {
        val p: PendingConnection = e.connection
        if (
            Config.globalEnabled && !Whitelist.contains("__global__", p.uniqueId.toString())
        ) {
            p.disconnect(
                TextComponent(
                    "&cKicked whilst connecting to BungeeCord:\n${Config.kickMessage}".getColored
                )
            )
        } else {
            UUIDCache.addOrRename(p.uniqueId.toString(), p.name)
        }
    }
}