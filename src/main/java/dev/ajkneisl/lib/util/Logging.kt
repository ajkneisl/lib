package dev.ajkneisl.lib.util

import dev.ajkneisl.lib.Lib
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Log [T] to the [Lib.DEFAULT_WEBHOOK]. */
suspend inline fun <reified T> T.webhook(): T {
    Lib.DEFAULT_WEBHOOK?.sendMessage(Json.encodeToString<T>(this))

    return this
}

/** Send a [message] through the webhook. */
suspend fun webhook(message: String) {
    Lib.DEFAULT_WEBHOOK?.sendMessage(message)
}
