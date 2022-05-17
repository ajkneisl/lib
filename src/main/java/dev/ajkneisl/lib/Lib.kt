package dev.ajkneisl.lib

import dev.ajkneisl.lib.discord.DiscordWebhook
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object Lib {
    internal val HTTP_CLIENT =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json)
            }
        }

    /** If this is set, then various log functions are able to work */
    var DEFAULT_WEBHOOK: DiscordWebhook? = null
}
