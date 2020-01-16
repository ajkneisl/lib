package dev.shog.lib

import dev.shog.lib.app.AppBuilder

object ShoLib {
    /**
     * The version of ShoLib
     */
    private const val VERSION = 1.0F

    val APP = AppBuilder()
            .withName("lib")
            .withVersion(VERSION)
            .withLogger()
            .checkUpdates(true)
            .build()

    init { APP }
}