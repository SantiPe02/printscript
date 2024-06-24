import com.github.ajalt.clikt.core.subcommands
import commands.Analyzing
import commands.Execute
import commands.Formatting
import commands.Printscript
import commands.Validate
import factory.LexerFactoryImpl
import interpreter.Interpreter
import parser.MyParser

val lexer = LexerFactoryImpl("1.1").create()

fun main(args: Array<String>): Unit =
    Printscript().subcommands(
        Validate(lexer, MyParser()),
        Formatting(),
        Analyzing(lexer, MyParser(), MyLinter()),
        Execute(lexer, MyParser(), Interpreter()),
    ).main(args)
