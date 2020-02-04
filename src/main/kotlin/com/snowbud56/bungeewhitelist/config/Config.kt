package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.ConfigBase
import com.snowbud56.bungeewhitelist.utils.getTarget

object Config: ConfigBase<ConfigData>(
    config = ConfigData(),
    target = getTarget("config.json")
)

data class ConfigData (
    var prefix: String = "&9&lWhitelist &8>> &7",
    var messageColor: String = "&7",
    var valueColor: String = "&c",
    var kickMessage: String = "&fYou are not whitelisted!",
    var globalEnabled: Boolean = false,
    var serverEnabled: MutableMap<String, Boolean> = mutableMapOf()
)