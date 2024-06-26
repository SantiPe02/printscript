package commands

import Linter
import ast.Range
import ast.Scope
import com.github.ajalt.clikt.testing.test
import factory.LexerFactoryImpl
import linterRules.LinterRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import result.validation.WarningResult
import java.io.File

class AnalyzingCommandTest {
    private val lexer = LexerFactoryImpl("1.0").create()

    @Test
    fun test001_ifCallAnalyzingCommandWithoutArgumentsFailWithUsageMessage() {
        val result = Analyzing(lexer, MyParser(), MockLinter(listOf())).test("")
        assertNotEquals(0, result.statusCode)
        assertTrue(result.output.contains("Usage"))
    }

    @Test
    fun test002_ifCallAnalyzingCommandWithoutExistingSourcecodeFileThenError() {
        val fileDir = "souce.ps"
        val configFile = File.createTempFile("config", ".json")
        val command = Analyzing(lexer, MyParser(), MockLinter(listOf()))

        val result = command.test("$fileDir ${configFile.path.replace("\\", "/")}")

        assertNotEquals(0, result.statusCode)
        assertEquals("The file could not be found in $fileDir\n", result.output)
        configFile.deleteOnExit()
    }

    @Test
    fun test003_ifCallAnalyzingCommandWithoutExistingConfigFileThenError() {
        val fileDir = "config.json"
        val sourceFile = File.createTempFile("source", ".ps")
        val command = Analyzing(lexer, MyParser(), MockLinter(listOf()))

        val result = command.test("${sourceFile.path.replace("\\", "/")} $fileDir")

        assertNotEquals(0, result.statusCode)
        assertEquals("The configuration file could not be found in $fileDir\n", result.output)
        sourceFile.deleteOnExit()
    }

    @Test
    fun test004_ifAllFilesExistAndSourceWithoutWarningThenCorrectMessage() {
        val sourceFile = File.createTempFile("source", ".ps")
        sourceFile.writeText("let a : number = 1;")
        val configFile = File.createTempFile("config", ".json")
        configFile.writeText("{\"one\":\"two\"}")
        val command = Analyzing(lexer, MyParser(), MockLinter(listOf()))

        val result = command.test("${sourceFile.path.replace("\\", "/")} ${configFile.path.replace("\\", "/")}")

        assertEquals(0, result.statusCode)
        assertEquals("Analyze finished with 0 warnings\n", result.output)
        configFile.deleteOnExit()
        sourceFile.deleteOnExit()
    }

    @Test
    fun test005_ifAllFilesExistAndSourceWithWarningThenCorrectMessageWithWarinings() {
        val sourceFile = File.createTempFile("source", ".ps")
        sourceFile.writeText("let a : number = 1;")
        val configFile = File.createTempFile("config", ".json")
        configFile.writeText("{\"one\":\"two\"}")
        val command =
            Analyzing(
                lexer,
                MyParser(),
                MockLinter(
                    listOf(
                        WarningResult(Range(0, 1), "warning1"),
                        WarningResult(Range(1, 2), "warning2"),
                    ),
                ),
            )

        val result = command.test("${sourceFile.path.replace("\\", "/")} ${configFile.path.replace("\\", "/")}")

        assertEquals(0, result.statusCode)
        assertEquals("Analyze finished with 2 warnings\nwarning1 in range 0:1\nwarning2 in range 1:2\n", result.output)
        configFile.deleteOnExit()
        sourceFile.deleteOnExit()
    }

    class MockLinter(val warnings: List<WarningResult>) : Linter {
        override fun lintScope(
            scope: Scope,
            rules: List<LinterRule>,
        ): List<WarningResult> = warnings
    }
}
