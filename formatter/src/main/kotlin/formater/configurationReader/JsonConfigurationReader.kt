package formater.configurationReader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import formater.FormatterConfiguration
import formater.IConfigurationReader
import java.io.File

object JsonConfigurationReader : IConfigurationReader {
    override fun getFileExtension(): String = "json"

    override fun readFileAndBuildRules(configFile: File): Result<FormatterConfiguration> {
        if (configFile.extension != "json") {
            return Result.failure(Exception("The method was expecting a json file but got a ${configFile.extension}"))
        }
        return try {
            Result.success(createGson().fromJson(configFile.readText(), FormatterConfiguration::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // TODO: refactor this, it's not pretty, maybe a builder pattern for the Config?
    private fun createGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(
            FormatterConfiguration::class.java,
            JsonDeserializer { json, _, _ ->
                val jsonObject = json.asJsonObject

                // standard values
                var spaceBeforeColon = true
                var spaceAfterColon = true
                var spaceBeforeAndAfterEqual = true
                var lineJumpsBeforePrintln = 1
                var indentation = 4

                val jsonFieldSpaceBeforeColon = jsonObject.get("SpaceBeforeColon")
                if (jsonFieldSpaceBeforeColon != null) {
                    try {
                        spaceBeforeColon = jsonFieldSpaceBeforeColon.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of Space Before Colon was not a boolean type")
                    }
                }

                val jsonFieldSpaceAfterColon = jsonObject.get("spaceAfterColon")
                if (jsonFieldSpaceAfterColon != null) {
                    try {
                        spaceAfterColon = jsonFieldSpaceAfterColon.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of Space After Colon was not a boolean type")
                    }
                }

                val jsonFieldSpaceEquals = jsonObject.get("spaceBeforeAndAfterEqual")
                if (jsonFieldSpaceEquals != null) {
                    try {
                        spaceBeforeAndAfterEqual = jsonFieldSpaceEquals.asBoolean
                    } catch (e: Exception) {
                        println("WARNING: the field of Space Before And After Equal was not a boolean type")
                    }
                }

                val jsonFieldLineJumpPrintln = jsonObject.get("lineJumpsBeforePrintln")
                if (jsonFieldLineJumpPrintln != null) {
                    try {
                        val lineJumpBeforePrintLineAux = jsonFieldLineJumpPrintln.asInt
                        if (lineJumpBeforePrintLineAux !in 0..3) {
                            println("WARNING: lineJumpBeforePrintln was not a value between 0 and 3 (inclusive)")
                        } else {
                            lineJumpsBeforePrintln = lineJumpBeforePrintLineAux
                        }
                    } catch (e: Exception) {
                        println("WARNING: the field of lineJumpBeforePrintln was not a int type")
                    }
                }

                val jsonFieldIndentation = jsonObject.get("indentation")
                if (jsonFieldIndentation != null) {
                    try {
                        val indentationAux = jsonFieldIndentation.asInt
                        if (indentationAux < 0) {
                            println("WARNING: indentation was not a positive value")
                        } else {
                            indentation = indentationAux
                        }
                    } catch (e: Exception) {
                        println("WARNING: the field of indentation was not a int type")
                    }
                }

                FormatterConfiguration(spaceBeforeColon, spaceAfterColon, spaceBeforeAndAfterEqual, lineJumpsBeforePrintln, indentation)
            },
        )
        return gsonBuilder.create()
    }
}
