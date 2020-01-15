package dev.shog.lib.app

import dev.shog.lib.ShoLibException
import dev.shog.lib.cache.Cache
import dev.shog.lib.cfg.Config
import dev.shog.lib.hook.DiscordWebhook
import reactor.core.publisher.Mono

/**
 * An applicaiton instance.
 *
 * @param applicationName The name of the application.
 * @param version The version of the application.
 * @param config The configuration of the application.
 * @param cache The cache of the application.
 * @param webhook The webhook of the application.
 */
class Application(
        private val applicationName: String,
        private val version: Float,
        private val config: Config? = null,
        private val cache: Cache? = null,
        private val webhook: DiscordWebhook? = null
) {
    /**
     * @return The cache
     * @throws Exception If cache wasn't set in builder.
     */
    fun getCache(): Cache =
            cache ?: throw ShoLibException("This application does not have a cache.")

    /**
     * @return Mono for sending the message
     * @throws Exception If webhook wasn't set in builder
     */
    fun sendMessage(message: String): Mono<Void> =
            getWebhook().sendMessage(message)

    /**
     * @return the Config object.
     * @throws Exception If config wasn't set in builder
     */
    inline fun <reified T> getConfigObject(): T =
            getConfig().asObject<T>()

    /**
     * @return The webhook
     * @throws Exception If webhook wasn't set in builder.
     */
    fun getWebhook(): DiscordWebhook =
            webhook ?: throw ShoLibException("This application does not have a webhook.")

    /**
     * @return [applicationName]
     */
    fun getName() =
            applicationName

    /**
     * @return [version]
     */
    fun getVersion() =
            version

    /**
     * Check for updates.
     */
    internal fun checkUpdates() {
        // TODO
    }

    /**
     * @return The config
     * @throws Exception If config wasn't set in builder.
     */
    fun getConfig(): Config =
            config ?: throw ShoLibException("This application does not have a config.")
}