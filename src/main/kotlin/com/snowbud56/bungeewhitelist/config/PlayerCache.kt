package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.ConfigBase
import com.snowbud56.bungeewhitelist.utils.getTarget

object PlayerCache: ConfigBase<MutableMap<String, String>>(
    config = mutableMapOf(),
    target = getTarget("playercache.json")
)