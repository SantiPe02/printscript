package formater.configurationReader

import formater.FormatterConfiguration
import formater.IConfigurationReader
import org.yaml.snakeyaml.Yaml
import java.io.File

object YamlConfigurationReader : IConfigurationReader {
    override fun getFileExtension(): String = "yaml"

    override fun readFileAndBuildRules(configFile: File): Result<FormatterConfiguration> {
        if (configFile.extension != getFileExtension()) {
            return Result.failure(Exception("The method was expecting a json file but got a ${configFile.extension}"))
        }

        val yaml = Yaml()
        val configMap = yaml.load<Map<String, Any>>(configFile.inputStream())

        val spaceBeforeColon = configMap["spaceBeforeColon"] as? Boolean ?: true
        val spaceAfterColon = configMap["spaceAfterColon"] as? Boolean ?: true
        val spaceBeforeAndAfterSpace = configMap["spaceBeforeAndAfterEqual"] as? Boolean ?: true
        val lineJumpBeforePrintln = (configMap["lineJumpBeforePrintln"] as? Int)?.takeIf { it in 0..3 } ?: 1
        val indentation = (configMap["indentation"] as? Int)?.takeIf { it in 0..8 } ?: 4

        return Result.success(
            FormatterConfiguration(
                spaceBeforeColon,
                spaceAfterColon,
                spaceBeforeAndAfterSpace,
                lineJumpBeforePrintln,
                indentation,
            ),
        )
    }
}
