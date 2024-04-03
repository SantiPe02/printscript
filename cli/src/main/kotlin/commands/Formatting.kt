package commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import formater.format
import java.io.File

class Formatting : CliktCommand() {
    val file : String by argument(help = "the file direction to format")
    val config : String by argument(help = "The configuration file for the formatter")

    override fun run() {
        val configFile = File(config)
        if (!configFile.exists()) throw CliktError("the configuration file was not found in $config")
        format(file, configFile).onFailure { throw CliktError(it.message) }
    }
}