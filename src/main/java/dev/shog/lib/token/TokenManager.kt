package dev.shog.lib.token

import dev.shog.lib.util.asDate
import dev.shog.lib.cache.Cache
import kong.unirest.Unirest
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Manages a token and makes sure it's always active.
 *
 * @param username The username of the account to get the token from.
 * @param password The SHA-512 hex of the password of the same account.
 */
class TokenManager(username: String, password: String, private val applicationName: String) {
    private var token: Token? = null

    /**
     * Get the actual token string from [token].
     *
     * This could throw an exception due to [token] not being initialized.
     */
    fun getProperToken(): String =
            token?.token!!

    init {
        val cache = Cache.forApplication(applicationName).getObject<Token>("token")
        val token = cache?.getValue()

        if (token != null && token.expiresOn - System.currentTimeMillis() > 0) {
            Timer().schedule(timerTask { renewToken().subscribe() }, token.expiresOn)

            this.token = token
        } else createToken(username, password).block() // needs to block :(
    }

    /**
     * Create a token using the shog.dev api.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     */
    private fun createToken(username: String, password: String): Mono<Void> =
            Unirest.post("https://api.shog.dev/v1/user")
                    .field("username", username)
                    .field("password", password)
                    .asJsonAsync()
                    .toMono()
                    .filter { req -> req.isSuccess }
                    .map { req -> req.body.`object`.getJSONObject("token")}
                    .doOnNext { token ->
                        Timer().schedule(
                                timerTask { renewToken().subscribe() },
                                token.getLong("expiresOn").asDate()
                        )
                    }
                    .doOnNext { token ->
                        val newToken = Token(
                                token.getString("token"),
                                token.getLong("owner"),
                                token.getLong("createdOn"),
                                token.getLong("expiresOn")
                        )

                        this.token = newToken
                        writeToken(newToken)
                    }
                    .then()

    /**
     * Write token to the cache, using [Cache].
     */
    private fun writeToken(token: Token) {
        val cache = Cache.forApplication(applicationName).getObject<Token>("token")

        if (cache == null) {
            Cache.forApplication(applicationName).createObject("token", token)
        } else cache.setValue(token)
    }

    /**
     * Renew [token].
     */
    private fun renewToken(): Mono<Void> =
            Unirest.patch("https://api.shog.dev/v1/token")
                    .header("Authorization", "token $token")
                    .asJsonAsync()
                    .toMono()
                    .filter { req -> req.body.`object`.getBoolean("successful") || !req.isSuccess }
                    .doOnNext { req ->
                        Timer().schedule(timerTask { renewToken().subscribe() }, req.body.`object`.getLong("newExpire").asDate())
                    }
                    .then()
}