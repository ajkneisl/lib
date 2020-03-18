package dev.shog.lib.app.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject

/**
 * The imported config from [ConfigHandler].
 */
class Config internal constructor(private val data: JSONObject) {
    /**
     * Get the config as a [JSONObject].
     */
    fun asJsonObject(): JSONObject =
            data

    /**
     * Get the config as [K].
     */
    inline fun <reified K> asObject(): K =
            ObjectMapper().readValue(asJsonObject().toString(), K::class.java)
}