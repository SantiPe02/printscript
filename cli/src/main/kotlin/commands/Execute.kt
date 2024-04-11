package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import interpreter.DefaultInterpreter
import interpreter.Report
import lexer.Lexer
import parser.Parser
import java.io.File

class Execute(val lexer: Lexer, val parser: Parser, val interpreter: DefaultInterpreter) : CliktCommand() {
    val fileDir: String by argument()

    override fun run() {
        val file = File(fileDir)
        if (!file.exists()) throw CliktError("The file to run was not found at $fileDir")

        interpreter.interpret(parser.parseTokens(lexer.tokenize(file.readText())))
        val report = Report(listOf(), listOf())
        report.outputs.forEach { out -> echo(out) }

        if (report.errors.isNotEmpty()) {
            throw CliktError(
                "Found errors in while executing:\n" + report.errors.joinToString("\n") { it },
            )
        }
    }
}
