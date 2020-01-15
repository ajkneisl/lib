package dev.shog.lib

import sun.awt.OSInfo
import java.io.File

object FileHandler {
     internal val SHOG_DEV_DIR = File(when (OSInfo.getOSType()) {
        OSInfo.OSType.WINDOWS -> "${System.getenv("appdata")}\\shogdev"
        OSInfo.OSType.LINUX -> "/etc/shogdev"

        else -> throw ShoLibException("Unknown operating system.")
    })

    /**
     * Get an application's folder using [SHOG_DEV_DIR]
     *
     * @param applicationName The name of the application.
     */
    fun getApplicationFolder(applicationName: String): File {
        if (applicationName.isBlank())
            throw ShoLibException("ApplicationName must not be blank!")

        val folder = File(SHOG_DEV_DIR.path + File.separator + applicationName)

        if (!folder.exists())
            folder.mkdirs()

        return folder
    }

    init {
        if (!SHOG_DEV_DIR.exists() && !SHOG_DEV_DIR.mkdir())
            throw ShoLibException("Could not create shogdev folder.")
    }
}