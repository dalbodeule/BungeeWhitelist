package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.ConfigBase
import com.snowbud56.bungeewhitelist.utils.getTarget

object Config: ConfigBase<ConfigData>(
    ConfigData(),
    getTarget("config.json")
) {
    val prefix: String
        get() = data.prefix

    val messageColor: String
        get() = data.messageColor

    val valueColor: String
        get() = data.valueColor

    val kickMessage: String
        get() = data.kickMessage

    var globalEnabled: Boolean
        get() = data.globalEnabled
        set(value) { data.globalEnabled = value }

    internal fun getServerStatus(server: String): Boolean {
        return data.serverEnabled[server] ?: false
    }

    internal fun setServerStatus(server: String, status: Boolean): Boolean {
        data.serverEnabled[server] = status

        return true
    }
}

data class ConfigData (
    var prefix: String = "&9&l[Whitelist]&r",
    var messageColor: String = "&7",
    var valueColor: String = "&c",
    var kickMessage: String = "&fYou are not whitelisted!",
    var globalEnabled: Boolean = false,
    var serverEnabled: MutableMap<String, Boolean> = mutableMapOf()
)