package dev.shog.lib.util

import dev.shog.lib.ShoLib.DEFAULT_LOGGER
import dev.shog.lib.app.Application
import org.apache.commons.lang3.exception.ExceptionUtils

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

/**
 * Log [T].
 */
fun <T> T.log(message: String): T {
    DEFAULT_LOGGER.info(message)
    return this
}

/**
 * Log [T] to the [DEFAULT_LOGGER].
 */
fun <T> T.logThis(): T {
    DEFAULT_LOGGER.info(this.toString())
    return this
}

/**
 * Log a [Throwable] to [Application.getLogger].
 */
fun <T : Throwable> T.logTo(app: Application): T {
    app.getLogger().error(ExceptionUtils.getMessage(this))
    return this
}

/**
 * Log [T] to [Application.getLogger]
 */
fun <T> T.logDiscord(app: Application): T {
    app.getWebhook().sendMessage("${app.getName()} (v${app.getVersion()})\n\n" + this.toString())
    return this
}

/**
 * Log a [Throwable] to [Application.getWebhook].
 */
fun <T : Throwable> T.logDiscord(app: Application): T {
    app.getWebhook().sendBigMessage(
            "[${app.getName()}:v${app.getVersion()}]:\n`${ExceptionUtils.getMessage(this)}`",
            ExceptionUtils.getStackTrace(this),
            "exception.txt"
    )
    return this
}