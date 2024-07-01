package commands

import com.github.ajalt.clikt.testing.test
import factory.InterpreterFactoryImpl
import factory.LexerFactoryImpl
import interpreter.Interpreter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import java.io.File

class ExecuteCommandTest {
    private val lexer = LexerFactoryImpl("1.0").create()
    val init = InterpreterFactoryImpl("1.1").create()

    @Test
    fun test01_WhenExecuteCommandWithoutArgThenFailWithMessage() {
        val result = Execute(lexer, MyParser(), Interpreter()).test("")
        assertTrue(result.statusCode != 0)
        assertTrue(result.output.contains("Usage"))
    }

    @Test
    fun test02_WhenExecuteCommandWithoutExistingSourceFileThenFailWithMessage() {
        val result = Execute(lexer, MyParser(), Interpreter()).test("source.ps")
        assertTrue(result.statusCode != 0)
        assertEquals("The file to run was not found at source.ps\n", result.output)
    }

    // TODO: change test for when a implementatio of prinLn is ready
    @Test
    fun test03_WhenExecuteCommandWithNiceSourceCodeThenReturnExecutionOutput() {
        val sourceFile = File.createTempFile("source", ".ps")
        sourceFile.writeText("let sum: number = ((3 + 5) * (2 + 4));")

        val command = Execute(lexer, MyParser(), Interpreter())
        val result = command.test(sourceFile.path.replace("\\", "/"))

        assertEquals(0, result.statusCode)
        assertEquals("", result.output)
        sourceFile.deleteOnExit()
    }

    @Test
    fun test04_WhenExecuteCommandWithErrorSourceCodeThenReturnExecutionOutput() {
        /** TODO: implement when interpreter is working correctly
         val sourceFile = File.createTempFile("source", ".ps")
         sourceFile.writeText("let sum: number = \"hi\";")

         val command = Execute(LexerImpl(), MyParser(), DefaultInterpreter())
         val result = command.test(sourceFile.path.replace("\\", "/"))

         assertEquals(0, result.statusCode)
         assertEquals("", result.output)
         sourceFile.deleteOnExit()
         **/
    }
}
