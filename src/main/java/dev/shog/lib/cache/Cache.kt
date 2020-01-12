package dev.shog.lib.cache

import dev.shog.lib.FileHandler
import dev.shog.lib.util.readObject
import dev.shog.lib.util.writeObject
import java.io.File

/**
 * Manages [CachedObject]s.
 */
class Cache(private val cacheDirectory: File = File(FileHandler.SHOG_DEV_DIR.path + File.separatorChar + "globalCache")) {
    init {
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs())
            throw Exception("Could not create cache directory.")
    }

    companion object {
        /**
         * Get a [Cache] instance for an application using [applicationName]
         */
        fun forApplication(applicationName: String): Cache =
                Cache(FileHandler.getApplicationFolder(applicationName))
    }

    /**
     * Get a [CachedObject] by their [key].
     *
     * @param key The key of the [CachedObject].
     * @return The found [CachedObject], otherwise null.
     */
    fun <T> getObject(key: String): CachedObject<T>? {
        val value = getValue<T>(key)
                ?: return null

        return CachedObject(key, value)
    }

    /**
     * Create a [CachedObject] using a [key].
     *
     * @param key The key for the newly created [CachedObject].
     * @param value The value for the [CachedObject].
     * @return The newly created [CachedObject]
     */
    fun <T> createObject(key: String, value: T): CachedObject<T>? {
        setValue(key, value)

        return CachedObject(key, value)
    }

    /**
     * Refresh a [CachedObject]'s value from file.
     *
     * @param cachedObject The cached object to refresh.
     */
    internal fun <T> refreshValue(cachedObject: CachedObject<T>) {
        val retrieved = getValue<T>(cachedObject.key)

        if (retrieved != null)
            cachedObject.value = retrieved
    }

    /**
     * Set a [CachedObject]'s value using [newObject].
     *
     * @param cachedObject The cached object to update.
     * @param newObject The new object.
     */
    internal fun <T> setValue(cachedObject: CachedObject<T>, newObject: T) {
        setValue(cachedObject.key, newObject)
        cachedObject.value = newObject
    }

    /**
     * Get a cache value.
     *
     * @param key The key of the cache file.
     * @return The value of the cache file.
     */
    private fun <T> getValue(key: String): T? {
        val file = File(cacheDirectory.path + File.separatorChar + "$key.shoch")

        return if (!file.exists())
            null
        else {
            file.readObject<T>()
        }
    }

    /**
     * Set a cache value.
     *
     * @param key The key of the cache file.
     * @param newValue The new value for the cache file.
     */
    private fun <T> setValue(key: String, newValue: T) {
        val file = File(cacheDirectory.path + File.separatorChar + "$key.shoch")

        if (!file.exists())
            file.createNewFile()

        file.writeObject(newValue)
    }
}