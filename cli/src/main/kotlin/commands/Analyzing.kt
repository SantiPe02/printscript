package commands

import Linter
import ast.Scope
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import configurationReader.ConfigurationReaderProvider
import lexer.Lexer
import parser.Parser
import translateFormatterConfigurationToRules
import java.io.File

class Analyzing(val lexer: Lexer, val parser: Parser, val linter: Linter) : CliktCommand() {
    val file: String by argument()
    val configFile: String by argument()

    override fun run() {
        val fileInstance = File(file)
        if (!fileInstance.exists()) throw CliktError("The file could not be found in $file")

        val configFile = File(configFile)
        if (!configFile.exists()) throw CliktError("The configuration file could not be found in $configFile")

        val reader =
            ConfigurationReaderProvider().getReader(configFile.extension).getOrElse {
                throw CliktError(it.message)
            }
        val config =
            reader.readFileAndBuildRules(configFile).getOrElse {
                throw CliktError(it.message)
            }
        val ast: Scope =
            parser.parseTokens(lexer.tokenize(fileInstance.readText())).getOrElse {
                throw CliktError(it.message)
            }
        val rules = translateFormatterConfigurationToRules(config)
        val warnings = linter.lintScope(ast, rules)
        echo("Analyze finished with ${warnings.size} warnings")
        warnings.forEach { warning -> echo("${warning.message} in range ${warning.range.start}:${warning.range.end}") }
    }
}
