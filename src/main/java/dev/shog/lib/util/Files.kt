package dev.shog.lib.util

import java.io.*

/**
 * Create a file from [String].
 */
fun String.toFile(): File =
        File(this)

/**
 * Write a serializable object to file.
 *
 * @param obj The object to write.
 */
fun <T> File.writeObject(obj: T) {
    if (!exists())
        createNewFile()

    val oos = ObjectOutputStream(FileOutputStream(this))

    oos.writeObject(obj)
}

/**
 * Read an object from a file.
 *
 * @return [T]. If file is not [T] or doesn't exist, null will be returned.
 */
fun <T> File.readObject(): T? {
    if (exists()) {
        try {
            val ois = ObjectInputStream(this.inputStream())

            return ois.readObject() as? T
        } catch (ex: Exception) {
        }
    }

    return null
}