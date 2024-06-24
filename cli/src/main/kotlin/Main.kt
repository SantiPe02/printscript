import com.github.ajalt.clikt.core.subcommands
import commands.Analyzing
import commands.Execute
import commands.Formatting
import commands.Printscript
import commands.Validate
import factory.InterpreterFactoryImpl
import factory.LexerFactoryImpl
import interpreter.Interpreter
import parser.MyParser

fun main(args: Array<String>) {
    val printscript = Printscript()
    printscript.parse(args.take(1).toTypedArray())

    val version = printscript.version

    val lexer = LexerFactoryImpl(version).create()
    InterpreterFactoryImpl(version).create()

    printscript.subcommands(
        Validate(lexer, MyParser()),
        Formatting(),
        Analyzing(lexer, MyParser(), MyLinter()),
        Execute(lexer, MyParser(), Interpreter()),
    ).main(args)
}
