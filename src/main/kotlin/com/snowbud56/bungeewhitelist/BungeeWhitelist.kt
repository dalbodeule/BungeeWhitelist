package com.snowbud56.bungeewhitelist

import com.snowbud56.bungeewhitelist.config.Config
import com.snowbud56.bungeewhitelist.config.UUIDCache
import com.snowbud56.bungeewhitelist.config.Whitelist
import com.snowbud56.bungeewhitelist.utils.getColored
import com.snowbud56.bungeewhitelist.commands.Whitelist as WhitelistCommand
import net.md_5.bungee.api.plugin.Plugin
import java.util.concurrent.TimeUnit

class BungeeWhitelist : Plugin() {
    companion object {
        lateinit var instance: BungeeWhitelist
    }

    override fun onEnable() {
        instance = this

        proxy.logger.info("${Config.config.prefix} BungeeWhitelist is enabled.".getColored)

        Config.load()
        Whitelist.load()
        UUIDCache.load()

        proxy.pluginManager.registerCommand(this, WhitelistCommand)
        proxy.pluginManager.registerListener(this, EventListener)

        proxy.scheduler.schedule(this, {
            Config.save()
            Whitelist.save()
            UUIDCache.save()
        }, 60, TimeUnit.SECONDS)
    }

    override fun onDisable() {
        Config.save()
        Whitelist.save()
        UUIDCache.save()

        proxy.logger.info("${Config.config.prefix} BungeeWhitelist is disabled.".getColored)
    }
}