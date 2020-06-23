package dev.shog.lib.util

import dev.shog.lib.ShoLib.DEFAULT_LOGGER
import dev.shog.lib.app.Application
import org.apache.commons.lang3.exception.ExceptionUtils

/**
 * Log [message] to [app].
 */
fun <T> T.logTo(app: Application, message: String): T {
    app.logger.debug(message)
    return this
}

/**
 * Log [T].
 */
fun <T> T.logThis(app: Application): T {
    app.logger.info(this.toString())
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
fun <T : Throwable> T.logTo(app: Application, fatal: Boolean = false): T {
    app.logger.error(fatal.eitherOr("FATAL: ", "") + ExceptionUtils.getMessage(this))
    return this
}

fun <T : Throwable> T.logThis(fatal: Boolean = false): T {
    DEFAULT_LOGGER.error(fatal.eitherOr("FATAL: ", "") + ExceptionUtils.getMessage(this))
    return this
}

/**
 * Log [T] to [Application.getLogger]
 */
fun <T> T.logDiscord(app: Application): T {
    app.sendMessage("${app.name}:${app.version}\n\n" + this.toString())
    return this
}

/**
 * Log a [Throwable] to [Application.getWebhook].
 */
fun <T : Throwable> T.logDiscord(app: Application): T {
    app.webhook.sendBigMessage(
            ExceptionUtils.getStackTrace(this),
            "${app.name}:${app.version}\n`${ExceptionUtils.getMessage(this)}`"
    )
    return this
}