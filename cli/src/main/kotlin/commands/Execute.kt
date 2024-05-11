package commands

import PartialStringReadingLexer
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser
import java.io.BufferedInputStream
import java.io.File
import java.nio.charset.Charset

class Execute(val lexer: Lexer, val parser: Parser, val interpreter: Interpreter) : CliktCommand() {
    val fileDir: String by argument()

    override fun run() {
        val file = File(fileDir)
        if (!file.exists()) throw CliktError("The file to run was not found at $fileDir")

        val report = interpretFile(file).report
        report.outputs.forEach { out -> echo(out) }

        if (report.errors.isNotEmpty()) {
            throw CliktError(
                "Found errors in while executing:\n" + report.errors.joinToString("\n") { it },
            )
        }
    }

    private fun interpretFile(file: File): Interpreter {
        if (file.extension != "ps") {
            throw CliktError("was expecting a ps file and got ${file.extension}")
        }

        file.inputStream().buffered().use { stream ->
            return interpretStream(stream)
        }
    }

    private fun interpretStream(stream: BufferedInputStream): Interpreter {
        val buffer = ByteArray(1046)
        var bytesRead = stream.read(buffer)
        var tokenizer = PartialStringReadingLexer(lexer)
        var interpreter = interpreter

        while (bytesRead != -1) {
            val textBlock = String(buffer, 0, bytesRead, Charset.defaultCharset())
            val tokenizerAndTokens = tokenizer.tokenizeString(textBlock)
            tokenizer = tokenizerAndTokens.first
            val tokens = tokenizerAndTokens.second

            val ast = parser.parseTokens(tokens).getOrElse { throw CliktError(it.message) }
            interpreter = interpreter.interpret(ast)
            bytesRead = stream.read(buffer)
        }
        stream.close()
        return interpreter
    }
}
