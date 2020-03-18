package dev.shog.lib.app

import dev.shog.lib.ShoLibException
import dev.shog.lib.app.cache.Cache
import dev.shog.lib.app.cfg.Config
import dev.shog.lib.hook.DiscordWebhook
import dev.shog.lib.hook.WebhookUser
import dev.shog.lib.util.eitherOr
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

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
     * The logger for the application.
     */
    private var logger: Logger? = null

    /**
     * What to do if the program is out of date.
     */
    private var updateHook: (Application.(newVersion: Float) -> Mono<Void>)? = null

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

    @Deprecated(message = "Replaced", replaceWith = ReplaceWith("setWebhook"))
    fun withWebhook(webhook: Config?.() -> DiscordWebhook): AppBuilder {
        this.webhook = webhook.invoke(config)
        return this
    }

    /**
     * Set webhook.
     *
     * @param webhook The parameter to get the [DiscordWebhook] instance.
     * @return The app builder.
     */
    fun setWebhook(webhook: (Config) -> DiscordWebhook): AppBuilder {
        if (config == null)
            throw ShoLibException("Config should be set before setting webhook!")

        this.webhook = webhook.invoke(config!!)
        return this
    }

    fun withLogger(name: String = applicationName): AppBuilder {
        if (!name.isBlank())
            this.logger = LoggerFactory.getLogger(name)

        return this
    }

    fun withUpdateHook(updateHook: (Application.(newVersion: Float) -> Mono<Void>)): AppBuilder {
        this.updateHook = updateHook
        return this
    }

    fun withWebhook(webhook: DiscordWebhook): AppBuilder {
        this.webhook = webhook
        return this
    }

    fun withWebhook(webhookUrl: String, discordUser: WebhookUser = DiscordWebhook.defaultUser): AppBuilder {
        this.webhook = DiscordWebhook(webhookUrl, discordUser)
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
        val cache = withCache.eitherOr(Cache.forApplication(applicationName), null)

        val app = Application(applicationName, currentVersion, config, cache, webhook, logger)

        if (shouldCheckUpdate)
            app.checkUpdates(updateHook ?: {  Mono.empty<Void>() })

        return app
    }
}