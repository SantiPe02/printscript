package formater.configurationReader

import formater.IConfigurationReader

class ConfigurationReaderProvider {
    fun getReader(extension: String): Result<IConfigurationReader> {
        return when (extension) {
            "json" -> Result.success(JsonConfigurationReader)
            "yaml" -> Result.success(YamlConfigurationReader)
            else -> Result.failure(ClassNotFoundException("No class found for that extension"))
        }
    }
}
