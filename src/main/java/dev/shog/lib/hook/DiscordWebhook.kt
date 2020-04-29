package dev.shog.lib.hook

import kong.unirest.Unirest
import org.json.JSONObject
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * The Discord Webhook Handler
 *
 * @param webhookUrl The URL for the webhook.
 */
class DiscordWebhook(private val webhookUrl: String) {
    /**
     * Send a file & optional message.
     *
     * @param file The file.
     * @param message The message.
     */
    fun sendFile(file: File, message: String = ""): CompletableFuture<Boolean> {
        if (message.length > 2000)
            return CompletableFuture.completedFuture(false)

        return Unirest.post(webhookUrl)
                .header("Content-Type", "application/json")
                .field("file", file)
                .field("content", message)
                .asEmptyAsync()
                .handleAsync { t, _ -> t.isSuccess }
    }

    /**
     * Send a message in the form of a file.
     *
     * @param bigMessage The large message.
     * @param message The message.
     */
     fun sendBigMessage(bigMessage: String, message: String = ""): CompletableFuture<Boolean> {
        if (message.length > 2000)
            return CompletableFuture.completedFuture(false)

        return Unirest.post(webhookUrl)
                .field("file", bigMessage.byteInputStream(), "message.txt")
                .field("content", message)
                .asStringAsync()
                .handleAsync { t, _ -> t.isSuccess }
    }

    /**
     * Send a message through the webhook.
     */
     fun sendMessage(message: String): CompletableFuture<Boolean> {
        if (message.length > 2000)
            return CompletableFuture.completedFuture(false)

        val obj = JSONObject().put("content", message)

        return Unirest.post(webhookUrl)
                .header("Content-Type", "application/json")
                .body(obj.toString())
                .asEmptyAsync()
                .handleAsync { t, _ -> t.isSuccess }
    }
}