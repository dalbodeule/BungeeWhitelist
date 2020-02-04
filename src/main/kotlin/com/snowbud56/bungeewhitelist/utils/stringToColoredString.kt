package com.snowbud56.bungeewhitelist.utils

import net.md_5.bungee.api.ChatColor

val String.getColoredString: String
    get() = ChatColor.translateAlternateColorCodes('&', this)