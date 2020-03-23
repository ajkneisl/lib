package dev.shog.lib.hook

import dev.shog.lib.hook.DiscordWebhook.Companion.defaultUser
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import org.json.JSONObject

/**
 * The Discord Webhook Handler
 *
 * @param webhookUrl The URL for the webhook.
 * @param user The user for the webhook. By default is [defaultUser].
 */
class DiscordWebhook(
        private val webhookUrl: String,
        private val httpClient: HttpClient,
        private val user: WebhookUser = defaultUser
) {
    /**
     * Send a message through the webhook.
     */
    suspend fun sendMessage(message: String): Boolean {
        if (message.length > 2000)
            return false

        val obj = getJsonObject()
                .put("content", message)

        try {
            httpClient.post<String>(webhookUrl) {
                header("Content-Type", "application/json")
                body = obj.toString()
            }

            return true
        } catch (e: Exception) {
        }

        return false
    }

    /**
     * Build the JSON object.
     */
    private fun getJsonObject(): JSONObject =
            JSONObject()
                    .put("username", user.username)
                    .put("avatar_url", user.imageUrl)
                    .put("tts", false)

    companion object {
        /**
         * The default user for the webhooks
         */
        internal val defaultUser = WebhookUser(
                "shog.dev",
                "https://cdn.discordapp.com/attachments/521062156024938498/636701089424605185/IMG_20191023_180024.jpg"
        )
    }
}