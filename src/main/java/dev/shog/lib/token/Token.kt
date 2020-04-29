package dev.shog.lib.token

import java.io.Serializable
import java.util.*

/**
 * An authorization token.
 */
data class Token(
        val token: String,
        val owner: UUID,
        val createdOn: Long,
        val expiresOn: Long
) : Serializable