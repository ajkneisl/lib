package dev.shog.lib.util

/**
 * If a [Collection] contains any null values.
 */
fun <T> Collection<T?>.hasNull(): Boolean =
        stream().anyMatch { obj -> obj == null }

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