package configurationReader

import IConfigurationReader
import LinterConfiguration
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import java.io.File

object JsonConfigurationReader : IConfigurationReader {
    override fun getFileExtension(): String = "json"

    override fun readFileAndBuildRules(configFile: File): Result<LinterConfiguration> {
        if (configFile.extension != "json") {
            return Result.failure(Exception("The method was expecting a json file but got a ${configFile.extension}"))
        }
        return try {
            Result.success(createGson().fromJson(configFile.readText(), LinterConfiguration::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // TODO: refactor this, it's not pretty, maybe a builder pattern for the Config?
    private fun createGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(
            LinterConfiguration::class.java,
            JsonDeserializer { json, _, _ ->
                val jsonObject = json.asJsonObject

                // standard values
                var camelCase = true
                var printlnWithoutExpression = true
                var undeclaredVariableStatement = true

                val jsonFieldSpaceBeforeColon = jsonObject.get("camelCase")
                if (jsonFieldSpaceBeforeColon != null) {
                    try {
                        camelCase = jsonFieldSpaceBeforeColon.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of Camel Case was not a boolean type")
                    }
                }

                val jsonFieldSpaceAfterColon = jsonObject.get("printlnWithoutExpression")
                if (jsonFieldSpaceAfterColon != null) {
                    try {
                        printlnWithoutExpression = jsonFieldSpaceAfterColon.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of println Without Expression was not a boolean type")
                    }
                }
                val jsonUndeclaredVariableStatement = jsonObject.get("undeclaredVariableStatement")
                if (jsonUndeclaredVariableStatement != null) {
                    try {
                        undeclaredVariableStatement = jsonUndeclaredVariableStatement.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of Undeclared Variable Statement was not a boolean type")
                    }
                }

                LinterConfiguration(camelCase, printlnWithoutExpression, undeclaredVariableStatement)
            },
        )
        return gsonBuilder.create()
    }
}
