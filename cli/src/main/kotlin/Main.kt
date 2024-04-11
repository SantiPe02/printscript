import com.github.ajalt.clikt.core.subcommands
import commands.Analyzing
import commands.Execute
import commands.Formatting
import commands.Printscript
import commands.Validate
import interpreter.DefaultInterpreter
import lexer.LexerImpl
import parser.MyParser

fun main(args: Array<String>): Unit =
    Printscript().subcommands(
        Validate(LexerImpl(), MyParser()),
        Formatting(),
        Analyzing(LexerImpl(), MyParser(), MyLinter()),
        Execute(LexerImpl(), MyParser(), DefaultInterpreter()),
    ).main(args)
