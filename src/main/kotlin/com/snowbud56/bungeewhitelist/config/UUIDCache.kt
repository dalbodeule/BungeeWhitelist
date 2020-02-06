package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.getTarget
import com.snowbud56.bungeewhitelist.utils.parseJSON
import com.snowbud56.bungeewhitelist.utils.serializeJSON
import java.nio.file.Files

object UUIDCache {
    private var data: MutableMap<String, String> = mutableMapOf()
    private val target = getTarget("UUIDCache.json")

    internal fun addOrRename(uuid: String, nick: String): Boolean {
        data[uuid] = nick.toLowerCase()

        return true
    }

    internal fun get(uuid: String): String? {
        return data[uuid]
    }

    internal fun remove(uuid: String): Boolean {
        data.remove(uuid)

        return true
    }

    internal fun getFromName(name: String): String? {
        return data.filterValues { it == name }.keys.firstOrNull()
    }

    internal fun load() {
        if (this.target.toFile().exists()) {
            data = parseJSON(
                Files.readAllBytes(this.target).toString(Charsets.UTF_8),
                data::class.java
            )
        }
    }

    internal fun save() {
        if (!this.target.toFile().exists()) {
            Files.createDirectories(this.target.parent)
            Files.createFile(this.target)
        }

        Files.write(this.target, data.serializeJSON().toByteArray())
    }
}
