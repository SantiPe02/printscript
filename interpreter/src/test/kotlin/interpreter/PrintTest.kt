package interpreter

import factory.LexerFactoryImpl
import interpreter.inputReaderImp.MockInputReader
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class PrintTest {
    private val lexer = LexerFactoryImpl("1.0").create()

    @Test
    fun test001_testFormatNumbers() {
        val code = """
            printLn("hello 4.0 there is 3.0 where 4.2")
            """

        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val newInterpreter = Interpreter(inputReader = MockInputReader(listOf())).interpret(it)

            Assertions.assertEquals("hello 4 there is 3 where 4.2", newInterpreter.report.outputs[0])
            Assertions.assertTrue(newInterpreter.report.errors.isEmpty())
        }
    }
}
