package dev.ajkneisl.lib.discord

import dev.ajkneisl.lib.Lib
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File
import kotlinx.coroutines.delay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

/**
 * The Discord Webhook Handler
 *
 * @param webhookUrl The URL for the webhook.
 */
class DiscordWebhook(private val webhookUrl: String) {
    companion object {
        /** The log level used for rate limit notifications */
        var RATE_LIMIT_LOG_LEVEL: Level? = Level.TRACE
    }

    private val logger = LoggerFactory.getLogger(this.javaClass)

    /** A webhook request. */
    @Serializable
    data class WebhookRequest(
        val content: String,
        val username: String,
        @SerialName("avatar_url") val avatarUrl: String,
    )

    /** Handle a request to the websocket. This handles the Discord provided rate limit. */
    private suspend fun handleRequest(request: suspend () -> String) {
        try {
            request.invoke()
        } catch (ex: ClientRequestException) {
            if (ex.response.status.value == 429) {
                val wait = ex.response.headers["X-RateLimit-Reset-After"]?.toInt()

                if (wait != null) {
                    when (RATE_LIMIT_LOG_LEVEL) {
                        Level.ERROR ->
                            logger.error("Webhook rate limit reached; waiting $wait seconds.")
                        Level.WARN ->
                            logger.warn("Webhook rate limit reached; waiting $wait seconds.")
                        Level.INFO ->
                            logger.info("Webhook rate limit reached; waiting $wait seconds.")
                        Level.DEBUG ->
                            logger.debug("Webhook rate limit reached; waiting $wait seconds.")
                        Level.TRACE ->
                            logger.trace("Webhook rate limit reached; waiting $wait seconds.")
                        else -> {}
                    }

                    delay(wait.toLong() * 1000)
                    request.invoke()
                }
            } else {
                logger.error(
                    "Webhook failed, ${ex.response.status.value} -> ${ex.response.status.description}")

                try {
                    logger.trace(ex.response.body())
                } catch (e: Exception) {
                    logger.error("The body of the error could not be received.")
                }
            }
        }
    }

    /**
     * Send a file & optional message.
     *
     * @param file The file.
     * @param message The message.
     */
    suspend fun sendFile(file: File, message: String = "") {
        if (message.length > 2000) {
            throw WebhookException("Message must be under 2000 characters!")
        }

        handleRequest {
            Lib.HTTP_CLIENT.post(webhookUrl) {
                contentType(ContentType.Application.Json)
                formData {
                    append("file", file.name, ContentType.defaultForFile(file)) {
                        append(String(file.readBytes()))
                    }
                    append("content", message)
                }
            }.body()
        }
    }

    /**
     * Send [message] to the webhook.
     *
     * @param message for the webhook. Must be under 2000 characters.
     */
    suspend fun sendMessage(message: String) {
        sendMessage(WebhookRequest(message, "", ""))
    }

    /**
     * Send a [request] through the webhook.
     * @param request The request for the webhook. Avatar URL and username aren't required.
     */
    suspend fun sendMessage(request: WebhookRequest) {
        if (request.content.length > 2000) {
            throw WebhookException("Message must be under 2000 characters!")
        }

        handleRequest {
            Lib.HTTP_CLIENT.post(webhookUrl) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }
    }

    /** Send a [bigMessage] with a [fileName] and optional [message]. */
    suspend fun sendBigMessage(bigMessage: String, fileName: String = "", message: String = "") {
        if (message.length > 2000) {
            throw WebhookException("Message must be under 2000 characters!")
        }

        handleRequest {
            Lib.HTTP_CLIENT.submitFormWithBinaryData(
                url = webhookUrl,
                formData = formData {
                    append("content", message)
                    append("file", bigMessage.toByteArray(), Headers.build {
                        append(HttpHeaders.ContentDisposition, "form-data; name=\"${fileName}\"; filename=\"${fileName}.txt\"")
                    })
                }
            ) {
                onUpload { bytesSentTotal, contentLength ->
                    logger.debug("Sent $bytesSentTotal bytes from $contentLength")
                }
            }.body()
        }
    }
}