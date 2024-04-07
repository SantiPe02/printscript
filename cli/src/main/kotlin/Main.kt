import com.github.ajalt.clikt.core.subcommands
import commands.Analyzing
import commands.Formatting
import commands.Printscript
import lexer.LexerImpl
import parser.MyParser

fun main(args: Array<String>): Unit =
    Printscript().subcommands(
        Formatting(),
        Analyzing(LexerImpl(), MyParser(), MyLinter()),
    ).main(args)
