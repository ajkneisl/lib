package dev.ajkneisl.lib

import dev.ajkneisl.lib.discord.DiscordWebhook
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.http.*

object Lib {
    internal val HTTP_CLIENT =
        HttpClient(CIO) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
                acceptContentTypes =
                    listOf(ContentType.Application.Json, ContentType.MultiPart.FormData)
            }
        }

    /** If this is set, then various log functions are able to work */
    var DEFAULT_WEBHOOK: DiscordWebhook? = null
}
