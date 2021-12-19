package dev.ajkneisl.lib.util

import java.io.*
import org.json.JSONObject

/** Create a file from [String]. */
fun String.toFile(): File = File(this)

/** Read a string from a [File]. */
fun File.readString(): String {
    return String(readBytes())
}

/** Read JSON from a [File] */
fun File.readJson(): JSONObject {
    return JSONObject(readString())
}

/**
 * Write a serializable object to file.
 *
 * @param obj The object to write.
 */
fun <T> File.writeObject(obj: T) {
    if (!exists()) createNewFile()

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
        } catch (ex: Exception) {}
    }

    return null
}
