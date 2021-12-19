package dev.ajkneisl.lib.util

import java.lang.Exception

/**
 * Get something from a [Map] using [key]. Instead of throwing an exception if the value is null, it
 * simply returns null.
 */
fun <T, K> Map<T, K>.getOrNull(key: T): K? =
    try {
        get(key)
    } catch (ex: Exception) {
        null
    }

/** If [Map] has any null keys. */
fun <T, K> Map<T?, K?>.hasNullKeys(): Boolean = keys.any { it == null }

/** If [Map] has any null objects. */
fun <T, K> Map<T?, K?>.hasNullObjects(): Boolean = values.any { it == null }

/** If [Map] has any null objects. */
fun <T, K> Map<T?, K?>.hasNullValues(): Boolean = hasNullKeys() || hasNullObjects()
