import com.github.ajalt.clikt.core.subcommands
import commands.Formatting
import commands.Printscript

fun main(args: Array<String>): Unit = Printscript().subcommands(
    Formatting()
).main(args)