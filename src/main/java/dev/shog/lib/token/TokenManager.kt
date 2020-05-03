package dev.shog.lib.token

import dev.shog.lib.ShoLibException
import dev.shog.lib.app.Application
import dev.shog.lib.util.asDate
import dev.shog.lib.util.blockingTimerTask
import kong.unirest.Unirest
import org.json.JSONObject
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Manages a token and makes sure it's always active.
 *
 * @param username The username of the account to get the token from.
 * @param password The SHA-512 hex of the password of the same account.
 */
class TokenManager(username: String, password: String, private val baseUrl: String = "https://api.shog.dev") {
    private var token = createToken(username, password).join()

    /**
     * The [Application] the token manager should log to.
     */
    var appLog: Application? = null

    /**
     * Get the actual token string from [token].
     *
     * This could throw an exception due to [token] not being initialized.
     */
    fun getToken(): String =
            token.token

    /**
     * Create a token using the shog.dev api.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     */
    private fun createToken(username: String, password: String): CompletableFuture<Token> =
            Unirest.post("${baseUrl}/v1/user")
                    .field("username", username)
                    .field("password", password)
                    .asStringAsync()
                    .handleAsync { result, _ ->
                        if (!result.isSuccess)
                            throw ShoLibException("Failed to create token! HTTP Response: ${result.body}")

                        val obj = JSONObject(result.body)
                        val token = obj.getJSONObject("payload").getJSONObject("token")

                        scheduleRenew(token.getLong("expiresOn").asDate())

                        val newToken = Token(
                                token.getString("token"),
                                UUID.fromString(token.getString("owner")),
                                token.getLong("createdOn"),
                                token.getLong("expiresOn")
                        )

                        this.token = newToken

                        newToken
                    }


    private fun scheduleRenew(expire: Date) {
        Timer().schedule(blockingTimerTask {
            renewToken()
        }, expire)
    }

    /**
     * Renew [token].
     */
    private fun renewToken(): CompletableFuture<Token> {
        return Unirest.patch("${baseUrl}/v1/token")
                .header("Authorization", "token $token")
                .asStringAsync()
                .handleAsync { result, _ ->
                    if (!result.isSuccess)
                        throw ShoLibException("Failed to renew token! HTTP Response: ${result.body}")

                    val payload = JSONObject(result.body).getJSONObject("payload")

                    val token = Token(
                            payload.getString("token"),
                            UUID.fromString(payload.getString("owner")),
                            payload.getLong("createdOn"),
                            payload.getLong("expiresOn")
                    )

                    this.token = token

                    scheduleRenew(token.expiresOn.asDate())

                    token
                }
    }
}