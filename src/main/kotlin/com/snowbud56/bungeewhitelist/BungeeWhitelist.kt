package com.snowbud56.bungeewhitelist

import com.snowbud56.bungeewhitelist.config.Config
import com.snowbud56.bungeewhitelist.config.Whitelist
import com.snowbud56.bungeewhitelist.utils.getColored
import com.snowbud56.bungeewhitelist.commands.Whitelist as WhitelistCommand
import net.md_5.bungee.api.plugin.Plugin

class BungeeWhitelist : Plugin() {
    companion object {
        lateinit var instance: BungeeWhitelist
    }

    override fun onEnable() {
        instance = this

        proxy.logger.info("${Config.config.prefix} BungeeWhitelist is enabled.".getColored)

        Config.load()
        Whitelist.load()

        proxy.pluginManager.registerCommand(this, WhitelistCommand)
        proxy.pluginManager.registerListener(this, EventListener)
    }

    override fun onDisable() {
        Config.save()
        Whitelist.save()

        proxy.logger.info("${Config.config.prefix} BungeeWhitelist is disabled.".getColored)
    }
}