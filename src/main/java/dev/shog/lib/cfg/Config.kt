package dev.shog.lib.cfg

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
     * Get the config as a [ShoConfig].
     *
     * @param cfg An empty ShoConfig to cast to.
     */
    fun asConfig(cfg: ShoConfig): ShoConfig =
            ObjectMapper().readValue(asJsonObject().toString(), cfg::class.java)

    /**
     * Get the config as [T].
     *
     * @param obj The class of [T].
     */
    fun <T> asObject(obj: Class<T>): T =
            ObjectMapper().readValue(asJsonObject().toString(), obj)
}