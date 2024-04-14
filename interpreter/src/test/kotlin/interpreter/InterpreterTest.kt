package interpreter

import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class InterpreterTest {
    private val interpreter = Interpreter()

    @Test
    fun test01_completeTestOfVersion1OfPrintscriptWhenValidCodeWithExitExpectedOfValue6() {
        val code = """
     let a: number = 12;
     let b: number = 4;
     let c: number;
     c = 3;
     a = a / b + c;

     println("Result: " + a);"""

        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val newInterpreter = interpreter.interpret(ast)

        assertEquals("Result: 6.0", newInterpreter.report.outputs[0])
        assertTrue(newInterpreter.report.errors.isEmpty())
    }
}
