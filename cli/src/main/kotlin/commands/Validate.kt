package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import lexer.Lexer
import parser.Parser
import java.io.File

class Validate(val lexer: Lexer, val parser: Parser) : CliktCommand() {
    val fileDir: String by argument()

    override fun run() {
        echo("===================")
        echo("started validation")
        val file = File(fileDir)
        if (!file.exists()) throw CliktError("The file to run was not found at $fileDir")

        parser.parseTokens(lexer.tokenize(file.readText())).getOrElse {
            // todo, should print the specific error
            throw CliktError("error found in validation")
        }
        echo("The file was validated correctly")
    }
}
