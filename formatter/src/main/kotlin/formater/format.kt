package formater

import formater.configurationReader.ConfigurationReaderProvider
import java.io.File

fun format(file : String, confifFile : File) : Result<Boolean> {
    val reader = ConfigurationReaderProvider().getReader(confifFile.extension).getOrElse { return Result.failure(it.cause!!) }
    val rules =translateFormatterConfigurationToRules(reader.readFileAndBuildRules(confifFile).getOrElse { return Result.failure(it.cause!!) })
    FileFormatter(RegexFormatter(rules)).format(file)
    return Result.success(true)
}