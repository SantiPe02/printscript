package commands

import ast.Range
import com.github.ajalt.clikt.testing.test
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import java.io.File

class AnalyzingCommandTest {
    @Test
    fun test001_ifCallAnalyzingCommandWithoutArgumentsFailWithUsageMessage() {
        val result = Analyzing(LexerImpl(), MyParser(), MockLinter(listOf())).test("")
        assertNotEquals(0, result.statusCode)
        assertTrue(result.output.contains("Usage"))
    }

    @Test
    fun test002_ifCallAnalyzingCommandWithoutExistingSourcecodeFileThenError() {
        val fileDir = "souce.ps"
        val configFile = File.createTempFile("config", ".json")
        val command = Analyzing(LexerImpl(), MyParser(), MockLinter(listOf()))

        val result = command.test("$fileDir ${configFile.path.replace("\\", "/")}")

        assertNotEquals(0, result.statusCode)
        assertEquals("The file could not be found in $fileDir\n", result.output)
    }

    @Test
    fun test003_ifCallAnalyzingCommandWithoutExistingConfigFileThenError() {
        val fileDir = "config.json"
        val sourceFile = File.createTempFile("source", ".ps")
        val command = Analyzing(LexerImpl(), MyParser(), MockLinter(listOf()))

        val result = command.test("${sourceFile.path.replace("\\", "/")} $fileDir")

        assertNotEquals(0, result.statusCode)
        assertEquals("The configuration file could not be found in $fileDir\n", result.output)
    }

    @Test
    fun test004_ifAllFilesExistAndSourceWithoutWarningThenCorrectMessage() {
        val sourceFile = File.createTempFile("source", ".ps")
        sourceFile.writeText("let a : number = 1;")
        val configFile = File.createTempFile("config", ".json")
        val command = Analyzing(LexerImpl(), MyParser(), MockLinter(listOf()))

        val result = command.test("${sourceFile.path.replace("\\", "/")} ${configFile.path.replace("\\", "/")}")

        assertEquals(0, result.statusCode)
        assertEquals("Analyze finished with 0 warnings\n", result.output)
    }

    @Test
    fun test005_ifAllFilesExistAndSourceWithWarningThenCorrectMessageWithWarinings() {
        val sourceFile = File.createTempFile("source", ".ps")
        sourceFile.writeText("let a : number = 1;")
        val configFile = File.createTempFile("config", ".json")
        val command =
            Analyzing(
                LexerImpl(),
                MyParser(),
                MockLinter(
                    listOf(WarningResult(Range(0, 1), "warning1"), WarningResult(Range(1, 2), "warning2")),
                ),
            )

        val result = command.test("${sourceFile.path.replace("\\", "/")} ${configFile.path.replace("\\", "/")}")

        assertEquals(0, result.statusCode)
        assertEquals("Analyze finished with 2 warnings\nwarning1 in range 0:1\nwarning2 in range 1:2\n", result.output)
    }
}
