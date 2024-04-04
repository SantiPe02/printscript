package formater

import java.io.File

interface IConfigurationReader {
    fun readFileAndBuildRules(configFile: File): Result<FormatterConfiguration>

    fun getFileExtension(): String
}
