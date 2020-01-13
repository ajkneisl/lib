package dev.shog.lib.hook

/**
 * A webhook user for [DiscordWebhook].
 *
 * @param username The username of the user.
 * @param imageUrl The URL of an image for the avatar.
 */
data class WebhookUser(val username: String, val imageUrl: String)