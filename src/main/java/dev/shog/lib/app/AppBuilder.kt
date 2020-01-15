package dev.shog.lib.app

import dev.shog.lib.cache.Cache
import dev.shog.lib.cfg.Config
import dev.shog.lib.hook.DiscordWebhook
import dev.shog.lib.util.eitherOr

/**
 * A builder for [Application].
 */
class AppBuilder {
    /**
     * If the application should check for updates on creation.
     */
    private var shouldCheckUpdate = false

    /**
     * The current version of the application.
     */
    private var currentVersion = 1.0F

    /**
     * The config for the application.
     */
    private var config: Config? = null

    /**
     * The webhook for the application.
     */
    private var webhook: DiscordWebhook? = null

    /**
     * If a cache should be added on creation.
     */
    private var withCache = false

    /**
     * The name of the application.
     */
    private var applicationName = ""

    fun withWebhook(webhook: Config?.() -> DiscordWebhook): AppBuilder {
        this.webhook = webhook.invoke(config)
        return this
    }

    fun withVersion(version: Float): AppBuilder {
        currentVersion = version
        return this
    }

    fun withCache(withCache: Boolean = true): AppBuilder {
        this.withCache = withCache
        return this
    }

    fun usingConfig(config: Config): AppBuilder {
        this.config = config
        return this
    }

    fun withName(name: String): AppBuilder {
        this.applicationName = name
        return this
    }

    fun checkUpdates(checkUpdates: Boolean): AppBuilder {
        this.shouldCheckUpdate = checkUpdates
        return this
    }

    /**
     * Build this into [Application].
     */
    fun build(): Application {
        if (applicationName == "")
            throw Exception("Name is unset!")

        val cache = withCache.eitherOr(Cache.forApplication(applicationName), null)

        val app = Application(applicationName, currentVersion, config, cache, webhook)

        if (shouldCheckUpdate)
            app.checkUpdates()

        return app
    }
}