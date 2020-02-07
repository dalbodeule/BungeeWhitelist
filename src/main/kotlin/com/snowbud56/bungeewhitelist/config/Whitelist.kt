package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.ConfigBase
import com.snowbud56.bungeewhitelist.utils.getTarget

object Whitelist: ConfigBase<MutableMap<String, MutableList<String>>>(
    mutableMapOf(),
    getTarget("whitelist.json")
) {
    internal fun getServer(server: String): MutableList<String> {
        if (!data.containsKey(server)) {
            data[server] = mutableListOf()
        }

        return data[server]!!
    }

    internal fun contains(server: String, uuid: String): Boolean {
        return getServer(server).contains(uuid)
    }

    internal fun add(server: String, uuid: String, name: String): Boolean {
        getServer(server).add(uuid)
        UUIDCache.addOrRename(uuid, name)

        return true
    }

    internal fun remove(server: String, uuid: String): Boolean {
        return getServer(server).remove(uuid)
    }
}