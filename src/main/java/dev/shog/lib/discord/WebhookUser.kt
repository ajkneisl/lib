package dev.shog.lib.discord

/**
 * A webhook user for [DiscordWebhook].
 *
 * @param username The username of the user.
 * @param image The URL of an image for the avatar.
 */
data class WebhookUser(val username: String, val image: String)