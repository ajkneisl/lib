package dev.shog.lib.util

/**
 * If a [Collection] contains any null values.
 */
fun <T> Collection<T?>.hasNull(): Boolean =
        stream()
                .anyMatch { obj -> obj == null }