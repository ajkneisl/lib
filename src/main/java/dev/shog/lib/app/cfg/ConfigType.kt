package dev.shog.lib.app.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.moandjiezana.toml.Toml
import dev.shog.lib.util.readJson
import dev.shog.lib.util.readString
import org.json.JSONObject
import java.io.File

sealed class ConfigType(val extension: String) {
    abstract fun parse(file: File): JSONObject
}

/**
 * A TOML config.
 */
object TomlConfig: ConfigType("toml") {
    override fun parse(file: File): JSONObject {
        val parsed = Toml().read(file)

        return JSONObject(parsed.toMap())
    }
}

/**
 * A YAML config.
 */
object YamlConfig: ConfigType("yml") {
    override fun parse(file: File): JSONObject {
        val json = ObjectMapper(YAMLFactory()).readTree(
                file.readString()
        )

        return JSONObject(json.toString())
    }
}

/**
 * A JSON config.
 */
object JsonConfig: ConfigType("json") {
    override fun parse(file: File): JSONObject {
        return file.readJson()
    }
}

class MalformedConfig: Throwable("There was an issue parsing the config.")