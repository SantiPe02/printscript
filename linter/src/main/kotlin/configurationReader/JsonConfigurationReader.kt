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
                var readInputWithoutExpression = true

                val jsonFieldCamelCase = jsonObject.get("camelCase")
                if (jsonFieldCamelCase != null) {
                    try {
                        camelCase = jsonFieldCamelCase.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of Camel Case was not a boolean type")
                    }
                }

                val jsonFieldPrintlnWithoutExpression = jsonObject.get("printlnWithoutExpression")
                if (jsonFieldPrintlnWithoutExpression != null) {
                    try {
                        printlnWithoutExpression = jsonFieldPrintlnWithoutExpression.asBoolean
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
                val jsonReadInputWithoutExpression = jsonObject.get("readInputWithoutExpression")
                if (jsonReadInputWithoutExpression != null) {
                    try {
                        readInputWithoutExpression = jsonReadInputWithoutExpression.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of Read Input Without Expression was not a boolean type")
                    }
                }

                LinterConfiguration(camelCase, printlnWithoutExpression, undeclaredVariableStatement, readInputWithoutExpression)
            },
        )
        return gsonBuilder.create()
    }
}
