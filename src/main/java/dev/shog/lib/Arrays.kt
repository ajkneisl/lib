package dev.shog.lib

/**
 * If a [Collection] contains any null values.
 */
fun <T> Collection<T?>.hasNull(): Boolean =
        stream()
                .anyMatch { obj -> obj == null }