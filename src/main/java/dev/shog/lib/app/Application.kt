package dev.shog.lib.app

import dev.shog.lib.app.cache.Cache
import dev.shog.lib.app.cfg.Config
import dev.shog.lib.discord.DiscordWebhook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

/**
 * An application.
 */
class Application internal constructor(
        val name: String,
        val version: String,
        val config: Config,
        webhook: (name: String, version: String, config: Config) -> DiscordWebhook
) {
    val cache = Cache.forApplication(name)
    val webhook = webhook.invoke(name, version, config)
    val logger = LoggerFactory.getLogger("$name:$version")

    /**
     * @return Mono for sending the message
     * @throws Exception If webhook wasn't set in builder
     */
    fun sendMessage(message: String): CompletableFuture<Boolean> =
            webhook.sendMessage(message)

    /**
     * @return the Config object.
     * @throws Exception If config wasn't set in builder
     */
    inline fun <reified T> getConfigObject(): T =
            config.asObject()
}