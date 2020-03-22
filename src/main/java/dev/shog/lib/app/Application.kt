package dev.shog.lib.app

import dev.shog.lib.ShoLibException
import dev.shog.lib.app.cache.Cache
import dev.shog.lib.app.cfg.Config
import dev.shog.lib.hook.DiscordWebhook
import dev.shog.lib.token.TokenManager
import io.ktor.client.HttpClient
import org.slf4j.Logger

/**
 * An application.
 */
class Application internal constructor(
        private val applicationName: String,
        private val version: Float,
        private val config: Config? = null,
        private val cache: Cache? = null,
        private val webhook: DiscordWebhook? = null,
        private val logger: Logger? = null,
        private val httpClient: HttpClient? = null,
        private val tokenManager: TokenManager? = null
) {
    /**
     * @return A Token Manager
     * @throws Exception If the token manager wasn't set in the builder
     */
    fun getTokenManager(): TokenManager =
            tokenManager ?: throw ShoLibException("This application does not have a Token Manager.")

    /**
     * @return The HTTP client
     * @throws Exception If HTTP client wasn't set in the builder.
     */
    fun getHttpClient(): HttpClient =
            httpClient ?: throw ShoLibException("This application does not have a HTTP Client.")

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
    suspend fun sendMessage(message: String): Boolean =
            getWebhook().sendMessage(message)

    /**
     * @return the Config object.
     * @throws Exception If config wasn't set in builder
     */
    inline fun <reified T> getConfigObject(): T =
            getConfig().asObject()

    /**
     * @return The webhook
     * @throws Exception If webhook wasn't set in builder.
     */
    fun getWebhook(): DiscordWebhook =
            webhook ?: throw ShoLibException("This application does not have a webhook.")

    /**
     * @return The logger
     * @throws Exception If logger wasn't set in builder.
     */
    fun getLogger(): Logger =
            logger ?: throw ShoLibException("This application does not have a logger.")

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
    internal fun checkUpdates(hook: (Application.(newVersion: Float) -> Unit), url: String = "http://localhost:8080") {
        // TODO
    }

    /**
     * @return The config
     * @throws Exception If config wasn't set in builder.
     */
    fun getConfig(): Config =
            config ?: throw ShoLibException("This application does not have a config.")
}