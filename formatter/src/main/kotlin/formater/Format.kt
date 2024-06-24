package formater

import factory.LexerFactoryImpl
import formater.astFormatter.ASTFormatter
import formater.configurationReader.ConfigurationReaderProvider
import parser.MyParser
import java.io.File

fun format(
    file: String,
    confifFile: File,
    version: String,
): Result<Boolean> {
    val lexer = LexerFactoryImpl(version).create()
    val reader = ConfigurationReaderProvider().getReader(confifFile.extension).getOrElse { return Result.failure(it) }
    val configurations =
        reader.readFileAndBuildRules(confifFile).getOrElse { return Result.failure(it) }
    FileFormatter(ASTFormatter(lexer, MyParser(), configurations)).format(file)
    return Result.success(true)
}
