package dev.shog.lib.app

import dev.shog.lib.ShoLibException
import dev.shog.lib.app.cache.Cache
import dev.shog.lib.app.cfg.Config
import dev.shog.lib.hook.DiscordWebhook
import dev.shog.lib.util.eitherOr
import org.slf4j.Logger

/**
 * A builder for [Application].
 */
class AppBuilder(val name: String, val version: Float) {
    var checkUpdates = false
    var config: Config? = null
    var logger: Logger? = null
    var updateHook: (Application.(newVersion: Float) -> Unit)? = null
    var webhook: DiscordWebhook? = null
    var useCache = false

    /**
     * Set [config].
     */
    fun usingConfig(config: Config): AppBuilder {
        this.config = config
        return this
    }

    /**
     * Configure with config.
     */
    fun configureConfig(conf: AppBuilder.(Config) -> Unit): AppBuilder {
        config.also { cfg ->
            if (cfg == null)
                throw ShoLibException("The config must be set before using configure!")

            conf.invoke(this, cfg)
        }
        return this
    }

    /**
     * Configure without config.
     */
    fun configure(conf: AppBuilder.() -> Unit): AppBuilder {
        conf.invoke(this)
        return this
    }

    /**
     * Build this into [Application].
     */
    fun build(): Application {
        val cache = useCache.eitherOr(Cache.forApplication(name), null)

        val app = Application(name, version, config, cache, webhook, logger)

        if (checkUpdates)
            app.checkUpdates(updateHook ?: { })

        return app
    }
}