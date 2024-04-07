package commands

import Linter
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import lexer.Lexer
import parser.Parser
import java.io.File

class Analyzing(val lexer: Lexer, val parser: Parser, val linter: Linter) : CliktCommand() {
    val file: String by argument()
    val configFile: String by argument()

    override fun run() {
        val fileInstance = File(file)
        if (!fileInstance.exists()) throw CliktError("The file could not be found in $file")

        val configFile = File(configFile)
        if (!configFile.exists()) throw CliktError("The configuration file could not be found in $configFile")

        // TODO("Get rules")
        val warnings = linter.lintScope(parser.parseTokens(lexer.tokenize(fileInstance.readText())), listOf())
        echo("Analyze finished with ${warnings.size} warnings")
        warnings.forEach { warning -> echo("${warning.message} in range ${warning.range.start}:${warning.range.end}") }
    }
}
