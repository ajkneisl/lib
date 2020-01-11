package dev.shog.lib

import java.lang.Exception

/**
 * Get something from a [Map] using [key].
 * Instead of throwing an exception if the value is null, it simply returns null.
 */
fun <T, K> Map<T, K>.getOrNull(key: T): K? =
        try {
            get(key)
        } catch (ex: Exception) {
            null
        }

