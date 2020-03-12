package dev.shog.lib.util

import dev.shog.lib.app.Application

/**
 * Log [message] to [app].
 */
fun <T> T.logTo(app: Application, message: String): T {
    app.getLogger().info(message)
    return this
}

/**
 * Log [T].
 */
fun <T> T.logThis(app: Application): T {
    app.getLogger().info(this.toString())
    return this
}