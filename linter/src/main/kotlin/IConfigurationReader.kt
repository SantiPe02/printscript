
import java.io.File

interface IConfigurationReader {
    fun getFileExtension(): String

    fun readFileAndBuildRules(configFile: File): Result<LinterConfiguration>
}
