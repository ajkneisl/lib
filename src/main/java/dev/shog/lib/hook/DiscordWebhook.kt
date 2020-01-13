package dev.shog.lib.hook

import dev.shog.lib.hook.DiscordWebhook.Companion.defaultUser
import kong.unirest.HttpResponse
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
    fun sendMessage(message: String): Mono<Void> =
            getJsonObject()
                    .doOnNext { js -> js.put("content", message) }
                    .flatMap { js -> makeRequest(js) }
                    .map { req -> parseResponse(req) ?: ":)" }
                    .doOnNext { resp -> if (resp != ":)") throw Exception("Invalid response from Discord.") }
                    .then()

    /**
     * Build the JSON object.
     */
    private fun getJsonObject(): Mono<JSONObject> =
            JSONObject()
                    .toMono()
                    .doOnNext { js -> js.put("username", user.username) }
                    .doOnNext { js -> js.put("avatar_url", user.imageUrl) }
                    .doOnNext { js -> js.put("tts", false) }

    /**
     * Parse the response given by Discord.
     *
     * If it's null, then the request is unsuccessful.
     */
    private fun parseResponse(resp: HttpResponse<String>): String? {
        return if (resp.status == 204)
            null
        else {
            return try {
                JSONObject(resp.body).toString()
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Creates the request.
     */
    private fun makeRequest(jsonObject: JSONObject): Mono<HttpResponse<String>> =
            Unirest.post(webhookUrl)
                    .header("Content-Type", "application/json")
                    .body(jsonObject.toString())
                    .asStringAsync()
                    .toMono()

    companion object {
        /**
         * The default user for the webhooks
         */
        private val defaultUser = WebhookUser(
                "buta",
                "https://cdn.discordapp.com/attachments/521062156024938498/636701089424605185/IMG_20191023_180024.jpg"
        )
    }
}