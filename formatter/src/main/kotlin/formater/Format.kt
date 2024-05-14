package formater

import formater.astFormatter.ASTFormatter
import formater.configurationReader.ConfigurationReaderProvider
import lexer.LexerImpl
import parser.MyParser
import java.io.File

fun format(
    file: String,
    confifFile: File,
): Result<Boolean> {
    val reader = ConfigurationReaderProvider().getReader(confifFile.extension).getOrElse { return Result.failure(it) }
    val configurations =
        reader.readFileAndBuildRules(confifFile).getOrElse { return Result.failure(it) }
    FileFormatter(ASTFormatter(LexerImpl(), MyParser(), configurations)).format(file)
    return Result.success(true)
}
