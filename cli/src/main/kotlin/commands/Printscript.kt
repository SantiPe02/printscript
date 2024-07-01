package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument

class Printscript : CliktCommand(name = "printScript") {
    val version: String by argument()

    override fun run() = Unit
}
