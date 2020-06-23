package dev.shog.lib

import dev.shog.lib.app.Application
import dev.shog.lib.app.cfg.ConfigHandler
import dev.shog.lib.app.cfg.ConfigType
import dev.shog.lib.discord.DiscordWebhook
import dev.shog.lib.discord.WebhookUser
import org.slf4j.LoggerFactory

object ShoLib {
    /**
     * Default logger
     */
    internal val DEFAULT_LOGGER = LoggerFactory.getLogger("SHGDEV")!!

    private val APP = Application(
            "shgdev",
            "1.3.0",
            ConfigHandler.useConfig(ConfigType.YML, "shgdev", ShoLibConfig())
    ) { name, ver, cfg ->
        val obj = cfg.asObject<ShoLibConfig>()

        DiscordWebhook(obj.webhook ?: "", WebhookUser("${name}:${ver}", "https://shog.dev/favicon.png"))
    }

    init { APP }

    private data class ShoLibConfig(val webhook: String? = null)
}