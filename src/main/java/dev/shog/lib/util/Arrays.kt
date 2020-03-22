package dev.shog.lib.util

import org.json.JSONArray

/**
 * If a [Collection] contains any null values.
 */
fun <T> Collection<T?>.hasNull(): Boolean =
        asSequence().any { obj -> obj == null }

/**
 * If a [Boolean] [Collection] contains a false boolean.
 */
fun Collection<Boolean>.anyFalse(): Boolean =
        asSequence().any { obj -> !obj }

/**
 * If a [String] [Collection] contains an empty string.
 */
fun Collection<String>.anyBlank(): Boolean =
        asSequence().any { obj -> obj.isBlank() }

/**
 * Turn a collection into a JSON array
 */
fun Collection<Any>.toJSON(): JSONArray {
    val array = JSONArray()

    asSequence()
            .forEach { obj -> array.put(obj) }

    return array
}