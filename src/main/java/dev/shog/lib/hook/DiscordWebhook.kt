package dev.shog.lib.hook

import dev.shog.lib.hook.DiscordWebhook.Companion.defaultUser
import kong.unirest.Unirest
import org.json.JSONObject
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * The Discord Webhook Handler
 *
 * @param webhookUrl The URL for the webhook.
 * @param user The user for the webhook. By default is [defaultUser].
 */
class DiscordWebhook(private val webhookUrl: String, private val user: WebhookUser = defaultUser) {
    /**
     * Send a message through the webhook.
     */
    fun sendMessage(message: String): Mono<Boolean> =
            getJsonObject()
                    .doOnNext { js -> js.put("content", message) }
                    .flatMap { js ->
                        Unirest.post(webhookUrl)
                                .header("Content-Type", "application/json")
                                .body(js.toString())
                                .asStringAsync()
                                .toMono()
                    }
                    .map { req -> req.isSuccess }

    /**
     * Build the JSON object.
     */
    private fun getJsonObject(): Mono<JSONObject> =
            JSONObject()
                    .toMono()
                    .doOnNext { js -> js.put("username", user.username) }
                    .doOnNext { js -> js.put("avatar_url", user.imageUrl) }
                    .doOnNext { js -> js.put("tts", false) }

    companion object {
        /**
         * The default user for the webhooks
         */
        internal val defaultUser = WebhookUser(
                "buta",
                "https://cdn.discordapp.com/attachments/521062156024938498/636701089424605185/IMG_20191023_180024.jpg"
        )
    }
}