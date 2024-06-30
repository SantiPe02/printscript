package interpreter

import interpreter.inputReaderImp.MockInputReader
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class ReadInputInterpreterTest {
    @Test
    fun test001_testGetAnInputString() {
        val code = """
            let a : string = readInput("What is your name?");
            """

        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val newInterpreter = Interpreter(inputReader = MockInputReader(listOf("General Kenobi"))).interpret(it)

            assertEquals("What is your name?", newInterpreter.report.outputs[0])
            assertEquals(Variable("string", "General Kenobi"), newInterpreter.variables["a"])
            assertTrue(newInterpreter.report.errors.isEmpty())
        }
    }

    @Test
    fun test002_testGetAnInputStringAndWorkWithIt() {
        val code = """
            let a : string = readInput("What is your name?");
            println("Hello there " + a);
            """

        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val newInterpreter = Interpreter(inputReader = MockInputReader(listOf("General Kenobi"))).interpret(it)

            assertEquals("What is your name?", newInterpreter.report.outputs[0])
            assertEquals("Hello there General Kenobi", newInterpreter.report.outputs[1])
            assertTrue(newInterpreter.report.errors.isEmpty())
        }
    }

    @Test
    fun test003_testParsingStringToOtherWhenCallingReadInput() {
        val code =
            """
            let a : number = readInput("");
            let b : boolean = readInput("");
            """.trimIndent()

        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val newInterpreter = Interpreter(inputReader = MockInputReader(listOf("10", "true"))).interpret(it)

            assertEquals(Variable("number", "10"), newInterpreter.variables["a"])
            assertEquals(Variable("boolean", "true"), newInterpreter.variables["b"])
            assertTrue(newInterpreter.report.errors.isEmpty())
        }
    }

    @Test
    fun test004_testFailedFromTSKWhereNameWasNotPrinted() {
        val code =
            """
            const name: string = readInput("Name:");
            println("Hello " + name + "!");
            """.trimIndent()

        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val newInterpreter = Interpreter(inputReader = MockInputReader(listOf("world"))).interpret(it)

            assertEquals("Name:", newInterpreter.report.outputs[0])
            assertEquals("Hello world!", newInterpreter.report.outputs[1])
            assertTrue(newInterpreter.report.errors.isEmpty())
        }
    }
}
