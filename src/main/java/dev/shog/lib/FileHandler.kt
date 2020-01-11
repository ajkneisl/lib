package dev.shog.lib

import dev.shog.lib.cache.Cache
import sun.awt.OSInfo
import java.io.File

internal object FileHandler {
    val SHOG_DEV_DIR = File(when (OSInfo.getOSType()) {
        OSInfo.OSType.WINDOWS -> "${System.getenv("appdata")}\\shogdev"
        OSInfo.OSType.LINUX -> "/etc/shogdev"

        else -> throw Exception("Unknown operating system.")
    })

    init {
        if (!SHOG_DEV_DIR.exists() && !SHOG_DEV_DIR.mkdir())
            throw Exception("Could not create shogdev folder.")
    }
}