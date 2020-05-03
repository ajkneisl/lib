package dev.shog.lib

import dev.shog.lib.app.AppBuilder
import org.slf4j.LoggerFactory

object ShoLib {
    private const val VERSION = 1.0F

    /**
     * Default logger
     */
    internal val DEFAULT_LOGGER = LoggerFactory.getLogger("SHGDEV")!!

    internal val APP = AppBuilder("shgdev.lib", VERSION)
            .configure { checkUpdates = true }
            .build()

    init { APP }
}