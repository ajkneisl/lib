package dev.shog.lib.global

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import dev.shog.lib.FileHandler
import dev.shog.lib.ShoLib
import dev.shog.lib.ShoLibException
import java.io.File

object GlobalVariableHandler {
    private val variablesFile = File(FileHandler.SHOG_DEV_DIR.path + File.separator + "gvars.yml")

    /**
     * The variables found in gvars.yml
     */
    private val variables: HashMap<String, Any>

    /**
     * Throw [ShoLibException] if [key] doesn't exist.
     */
    @Throws(ShoLibException::class)
    fun getVar(key: String): Any =
            variables[key] ?: throw ShoLibException("That variable doesn't exist!")

    init {
        val mapper = ObjectMapper(YAMLFactory())

        if (variablesFile.exists()) {
            variables = mapper.readValue(
                    variablesFile,
                    mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, Any::class.java)
            )
        } else {
            ShoLib.DEFAULT_LOGGER.debug("Creating global variables YML file...")

            variablesFile.createNewFile()
            variables = hashMapOf("pog" to "champ")

            mapper.writeValue(variablesFile, variables)
        }
    }
}