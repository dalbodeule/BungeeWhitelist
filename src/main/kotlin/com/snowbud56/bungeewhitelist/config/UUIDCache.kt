package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.ConfigBase
import com.snowbud56.bungeewhitelist.utils.getTarget

object UUIDCache: ConfigBase<MutableMap<String, String>>(
    mutableMapOf(),
    getTarget("UUIDCache.json")
) {
    internal fun addOrRename(uuid: String, nick: String): Boolean {
        data[uuid] = nick.toLowerCase()

        return true
    }

    internal fun get(uuid: String): String? {
        return data[uuid]
    }

    internal fun getNames(): List<String> {
        return data.values.toList()
    }

    internal fun remove(uuid: String): Boolean {
        data.remove(uuid)

        return true
    }

    internal fun getFromName(name: String): String? {
        return data.filterValues { it == name }.keys.firstOrNull()
    }
}
