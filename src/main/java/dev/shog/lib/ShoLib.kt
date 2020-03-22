package dev.shog.lib

import dev.shog.lib.app.AppBuilder
import org.slf4j.LoggerFactory

object ShoLib {
    /**
     * The version of ShoLib
     */
    private const val VERSION = 1.0F

    /**
     * Default logger
     */
    val DEFAULT_LOGGER = LoggerFactory.getLogger("SHGDEV")!!

    val APP = AppBuilder()
            .configure {
                name = "shgdev.lib"
                version = VERSION
                checkUpdates = true
            }
            .build()

    init { APP }
}