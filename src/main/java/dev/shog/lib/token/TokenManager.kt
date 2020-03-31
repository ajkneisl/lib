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
class TokenManager(username: String, password: String, private val application: Application) {
    private var token: Token? = null

    /**
     * Get the actual token string from [token].
     *
     * This could throw an exception due to [token] not being initialized.
     */
    fun getToken(): String =
            token?.token!!

    init {
        val cache = try {
            application.getCache().getObject<Token>("token")
        } catch (e: Exception) {
            throw ShoLibException("A token manager couldn't be made due to the application not having a cache!")
        }

        val token = cache?.getValue()

        if (token != null) runBlocking {
                renewToken()
                this@TokenManager.token = token
        } else runBlocking {
            createToken(username, password)
        }
    }

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
        writeToken(newToken)
    }

    /**
     * Write token to the cache, using [Cache].
     */
    private fun writeToken(token: Token) {
        val cache = application.getCache().getObject<Token>("token")

        if (cache == null) {
            application.getCache().createObject("token", token)
        } else cache.setValue(token)
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
        writeToken(token)
    }
}