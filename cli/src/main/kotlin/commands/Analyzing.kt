package commands

import ast.AST
import ast.Range
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
        val warnings = linter.lint(parser.parseTokens(lexer.tokenize(fileInstance.readText())), listOf())
        echo("Analyze finished with ${warnings.size} warnings")
        warnings.forEach { warning -> echo("${warning.message} in range ${warning.range.start}:${warning.range.end}") }
    }
}

sealed interface Linter {
    fun lint(
        ast: AST,
        rules: List<LinterRule>,
    ): List<WarningResult>
}

class MockLinter(val warnings: List<WarningResult>) : Linter {
    override fun lint(
        ast: AST,
        rules: List<LinterRule>,
    ): List<WarningResult> = warnings
}

sealed interface LinterRule {
    fun ruleIsValid(tree: AST): ValidationResult
}

sealed interface ValidationResult

class WarningResult(val range: Range, val message: String) : ValidationResult {
    fun getWarning(): Pair<Range, String> = Pair(range, message)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WarningResult) return false

        if (range != other.range) return false
        if (message != other.message) return false

        return true
    }
}
