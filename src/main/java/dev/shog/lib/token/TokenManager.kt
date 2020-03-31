package dev.shog.lib.token

import dev.shog.lib.ShoLibException
import dev.shog.lib.app.Application
import dev.shog.lib.app.cache.Cache
import dev.shog.lib.util.asDate
import dev.shog.lib.util.getAge
import kong.unirest.Unirest
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Manages a token and makes sure it's always active.
 *
 * @param username The username of the account to get the token from.
 * @param password The SHA-512 hex of the password of the same account.
 */
class TokenManager(username: String, password: String) {
    private var token: Token? = null

    /**
     * Get the actual token string from [token].
     *
     * This could throw an exception due to [token] not being initialized.
     */
    fun getToken(): String =
            token?.token!!

    init { createToken(username, password) }

    /**
     * Create a token using the shog.dev api.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     */
    private fun createToken(username: String, password: String) {
        val result = Unirest.post("https://api.shog.dev/v1/user")
                .field("username", username)
                .field("password", password)
                .asString()

        if (!result.isSuccess)
            throw ShoLibException("Failed to create token!")

        val obj = JSONObject(result.body)
        val token = obj.getJSONObject("payload").getJSONObject("token")

        Timer().schedule(timerTask {
            runBlocking { renewToken()  }
        }, token.getLong("expiresOn").asDate())

        val newToken = Token(
                token.getString("token"),
                token.getLong("owner"),
                token.getLong("createdOn"),
                token.getLong("expiresOn")
        )

        this.token = newToken
    }

    /**
     * Renew [token].
     */
    private suspend fun renewToken() {
        val result = Unirest.patch("https://api.shog.dev/v1/token")
                .header("Authorization", "token $token")
                .asString()

        if (!result.isSuccess)
            throw ShoLibException("Failed to renew token!")

        val payload = JSONObject(result.body).getJSONObject("payload")

        Timer().schedule(timerTask {
            runBlocking { renewToken() }
        }, payload.getLong("newExpire").asDate())

        val token = Token(
                payload.getString("token"),
                payload.getLong("owner"),
                payload.getLong("createdOn"),
                payload.getLong("expiresOn")
        )

        this.token = token
    }
}