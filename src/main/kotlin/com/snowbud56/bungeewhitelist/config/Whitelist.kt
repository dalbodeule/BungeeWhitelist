package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.ConfigBase
import com.snowbud56.bungeewhitelist.utils.getTarget

object Whitelist: ConfigBase<MutableMap<String, MutableList<String>>>(
    config = mutableMapOf(),
    target = getTarget("whitelist.json")
)