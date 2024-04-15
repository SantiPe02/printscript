package configurationReader

import IConfigurationReader
import LinterConfiguration
import org.yaml.snakeyaml.Yaml
import java.io.File

object YamlConfigurationReader : IConfigurationReader {
    override fun getFileExtension(): String = "yaml"

    override fun readFileAndBuildRules(configFile: File): Result<LinterConfiguration> {
        if (configFile.extension != getFileExtension()) {
            return Result.failure(Exception("The method was expecting a yaml file but got a ${configFile.extension}"))
        }

        val yaml = Yaml()
        val configMap = yaml.load<Map<String, Any>>(configFile.inputStream())

        val camelCaseRule = configMap["camelCase"] as? Boolean ?: true
        val printlnWithoutException = configMap["printlnWithoutException"] as? Boolean ?: true
        val undeclaredVariableStatement = configMap["undeclaredVariableStatement"] as? Boolean ?: true

        return Result.success(
            LinterConfiguration(camelCaseRule, printlnWithoutException, undeclaredVariableStatement),
        )
    }
}
