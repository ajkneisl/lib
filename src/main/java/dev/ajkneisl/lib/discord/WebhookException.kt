package dev.ajkneisl.lib.discord

import dev.ajkneisl.lib.LibException

/** An exception that occurs in [DiscordWebhook]. */
class WebhookException(override val message: String) : LibException()
