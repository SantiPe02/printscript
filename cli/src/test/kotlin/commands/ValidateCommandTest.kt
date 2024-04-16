package commands

import com.github.ajalt.clikt.testing.test
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import java.io.File

class ValidateCommandTest {
    @Test
    fun test001_whenValidatingCommandWithoutArgsThenCommandFailWithMessage() {
        val result = Validate(LexerImpl(), MyParser()).test("")
        assertTrue(result.statusCode != 0)
        assertTrue(result.output.contains("Usage"))
    }

    @Test
    fun test02_WhenValidateCommandWithoutExistingSourceFileThenFailWithMessage() {
        val result = Validate(LexerImpl(), MyParser()).test("source.ps")
        assertTrue(result.statusCode != 0)
        assertTrue(result.output.contains("The file to run was not found at source.ps\n"))
    }

    @Test
    fun test03_WhenValidateCommandWithNiceSourceCodeThenSuccessMessage() {
        val sourceFile = File.createTempFile("source", ".ps")
        sourceFile.writeText("let sum: number = ((3 + 5) * (2 + 4));")

        val command = Validate(LexerImpl(), MyParser())
        val result = command.test(sourceFile.path.replace("\\", "/"))

        assertEquals(0, result.statusCode)
        assertTrue(result.output.contains("The file was validated correctly"))
        sourceFile.deleteOnExit()
    }

    @Test
    fun test04_WhenFileWithMissingSemicolonValidatedThenErrorMessage() {
        val sourceFile = File.createTempFile("source", ".ps")
        sourceFile.writeText("let sum: number = ((3 + 5) * (2 + 4))")

        val command = Validate(LexerImpl(), MyParser())
        val result = command.test(sourceFile.path.replace("\\", "/"))

        assertTrue(result.statusCode != 0)
        assertTrue(result.output.contains("error found in validation"))
        sourceFile.deleteOnExit()
    }
}
