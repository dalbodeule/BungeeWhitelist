package com.snowbud56.bungeewhitelist.config

import com.snowbud56.bungeewhitelist.utils.getTarget
import com.snowbud56.bungeewhitelist.utils.parseJSON
import com.snowbud56.bungeewhitelist.utils.serializeJSON
import java.nio.file.Files
import java.nio.file.Path

object Whitelist {
    private var data: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val target: Path = getTarget("whitelist.json")

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