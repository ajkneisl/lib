package dev.shog.lib.hook

import dev.shog.lib.hook.DiscordWebhook.Companion.defaultUser
import dev.shog.lib.util.ifSo
import kong.unirest.Unirest
import org.json.JSONObject
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * The Discord Webhook Handler
 *
 * @param webhookUrl The URL for the webhook.
 * @param user The user for the webhook. By default is [defaultUser].
 */
class DiscordWebhook(
        private val webhookUrl: String,
        private val user: WebhookUser = defaultUser
) {
    /**
     * Send a file & optional message.
     *
     * @param file The file.
     * @param message The message.
     */
    fun sendFile(file: File, message: String = ""): CompletableFuture<Boolean> {
        if (message.length > 2000 || !file.exists())
            return CompletableFuture.completedFuture(false)

        val bytes = file.inputStream().readBytes()

        return Unirest.post(webhookUrl)
                .ifSo(message.isNotBlank()) { field("payload_json", getJsonObject().put("content", message)) }
                .field("file", bytes, file.name)
                .asEmptyAsync()
                .handleAsync { t, _ -> t.isSuccess }
    }

    /**
     * Send a message through a file. This avoids the 2000 character limit.
     *
     * @param message A small message.
     * @param file A large message.
     * @param fileName The file's name.
     */
    fun sendBigMessage(message: String, file: String, fileName: String = "content.txt"): CompletableFuture<Boolean> {
        if (message.length > 2000)
            return CompletableFuture.completedFuture(false)

        val bytes = file.toByteArray()

        return Unirest.post(webhookUrl)
                .field("payload_json", getJsonObject().put("content", message))
                .field("file", bytes, fileName)
                .asEmptyAsync()
                .handleAsync { t, _ -> t.isSuccess }
    }

    /**
     * Send a message through the webhook.
     */
    fun sendMessage(message: String): CompletableFuture<Boolean> {
        if (message.length > 2000)
            return CompletableFuture.completedFuture(false)

        val obj = getJsonObject()
                .put("content", message)

        return Unirest.post(webhookUrl)
                .header("Content-Type", "application/json")
                .body(obj.toString())
                .asEmptyAsync()
                .handleAsync { t, _ -> t.isSuccess  }
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