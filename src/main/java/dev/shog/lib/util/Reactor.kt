package dev.shog.lib.util

import dev.shog.lib.app.Application
import reactor.core.publisher.Mono

/**
 * Log in a [Mono].
 */
fun <T> Mono<T>.logTo(app: Application, message: String): Mono<T> =
        doOnNext { app.getLogger().info(message) }