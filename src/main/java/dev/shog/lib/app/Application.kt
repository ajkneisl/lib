package dev.shog.lib.app

import dev.shog.lib.cache.Cache
import dev.shog.lib.cfg.Config
import dev.shog.lib.hook.DiscordWebhook

class Application(
        private val applicationName: String,
        private val version: Float,
        private val config: Config? = null,
        private val cache: Cache? = null,
        private val webhook: DiscordWebhook? = null
) {
    fun getCache(): Cache =
            cache ?: throw Exception("This application does not have a cache.")

    fun getWebhook(): DiscordWebhook =
            webhook ?: throw Exception("This application does not have a webhook.")

    fun getName() =
            applicationName

    fun getVersion() =
            version

    /**
     * Check for updates.
     */
    internal fun checkUpdates() {
        // TODO
    }

    fun getConfig(): Config =
            config ?: throw Exception("This application does not have a config.")
}