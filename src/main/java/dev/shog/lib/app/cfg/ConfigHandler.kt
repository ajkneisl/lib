package dev.shog.lib.app.cfg

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.moandjiezana.toml.TomlWriter
import dev.shog.lib.FileHandler
import dev.shog.lib.ShoLibException
import java.io.File
import java.lang.Exception

/**
 * Manages configuration files.
 */
object ConfigHandler {
    /**
     * Get a config using a [configType] and [applicationName].
     *
     * @param configType The type the config is.
     * @param applicationName The name of the application.
     * @return A [Config] instance.
     */
    @Throws(ShoLibException::class)
    private fun getConfig(configType: ConfigType, applicationName: String): Config {
        val file = File(FileHandler.getApplicationFolder(applicationName).path + File.separator + "cfg." + configType.extension)

        return try {
            Config(configType.parse(file))
        } catch (ex: Exception) {
            throw MalformedConfig()
        }
    }

    /**
     * Create a config using [configType] and [applicationName].
     *
     * @param configType The type of config to create.
     * @param cfg The object to write.
     * @param applicationName The application to create the config for.
     * @param overwrite If the config exists, overwrite it? If this is false, this will still return a [Config] instance.
     * @throws ShoLibException If there's an issue writing the config.
     * @return A [Config] instance.
     */
    @Throws(ShoLibException::class)
    fun <T> useConfig(configType: ConfigType, applicationName: String, cfg: T, overwrite: Boolean = false): Config {
        val file = File(FileHandler.getApplicationFolder(applicationName).path + File.separator + "cfg." + configType.extension)

        if (file.exists() && !overwrite)
            return getConfig(configType, applicationName)

        file.createNewFile()

        try {
            when (configType) {
                is TomlConfig -> TomlWriter().write(cfg, file)
                is YamlConfig -> ObjectMapper(YAMLFactory()).writeValue(file, cfg)
                is JsonConfig -> ObjectMapper().writeValue(file, cfg)
            }
        } catch (ex: Exception) {
            throw ShoLibException("There was an issue writing the config.")
        }

        return getConfig(configType, applicationName)
    }
}