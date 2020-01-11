package dev.shog.lib.cache

/**
 * A cached object.
 *
 * This can be found in either `/etc/shogdev/ or `%appdata%\shogdev\`.
 *
 * @param key The key of the cached object.
 * @param value The value of the cached object.
 */
class CachedObject<T> internal constructor(internal val key: String, internal var value: T) {
    /**
     * Get [value].
     */
    fun getValue(): T =
            value

    /**
     * Refresh [value] from file.
     */
    fun refreshValue() {
        Cache.refreshValue(this)
    }

    /**
     * Set a new value for [value].
     */
    fun setValue(newValue: T) {
        Cache.setValue(this, newValue)
    }
}