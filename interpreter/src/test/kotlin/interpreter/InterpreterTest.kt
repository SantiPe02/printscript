package interpreter

import ast.BooleanCondition
import ast.Call
import ast.IfAndElseStatement
import ast.IfStatement
import ast.LiteralArgument
import ast.Range
import ast.Scope
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
        ast.onSuccess {
            val newInterpreter = interpreter.interpret(it)

            assertEquals("Result: 6.0", newInterpreter.report.outputs[0])
            assertTrue(newInterpreter.report.errors.isEmpty())
        }
    }

    @Test
    fun test002_testIfStatementIfTrue() {
        val ast =
            Scope(
                "file",
                Range(0, 0),
                listOf(
                    IfStatement(
                        Range(0, 0),
                        listOf(BooleanCondition(Range(0, 0), LiteralArgument(Range(0, 0), "true", "boolean"))),
                        Scope(
                            "ifScope",
                            Range(0, 0),
                            listOf(Call(Range(0, 0), "println", listOf(LiteralArgument(Range(0, 0), "success", "string")))),
                        ),
                    ),
                ),
            )
        val newInterpreter = interpreter.interpret(ast)
        assertEquals("success", newInterpreter.report.outputs.first())
    }

    @Test
    fun test003_testIfStatementIfFalse() {
        val ast =
            Scope(
                "file",
                Range(0, 0),
                listOf(
                    IfStatement(
                        Range(0, 0),
                        listOf(BooleanCondition(Range(0, 0), LiteralArgument(Range(0, 0), "false", "boolean"))),
                        Scope(
                            "ifScope",
                            Range(0, 0),
                            listOf(Call(Range(0, 0), "println", listOf(LiteralArgument(Range(0, 0), "success", "string")))),
                        ),
                    ),
                ),
            )
        val newInterpreter = interpreter.interpret(ast)
        assertTrue(newInterpreter.report.outputs.isEmpty())
    }

    @Test
    fun test004_testIfAndElseStatementIfTrue() {
        val ast =
            Scope(
                "file",
                Range(0, 0),
                listOf(
                    IfAndElseStatement(
                        Range(0, 0),
                        listOf(BooleanCondition(Range(0, 0), LiteralArgument(Range(0, 0), "true", "boolean"))),
                        Scope(
                            "ifScope",
                            Range(0, 0),
                            listOf(Call(Range(0, 0), "println", listOf(LiteralArgument(Range(0, 0), "success", "string")))),
                        ),
                        Scope(
                            "elseScope",
                            Range(0, 0),
                            listOf(Call(Range(0, 0), "println", listOf(LiteralArgument(Range(0, 0), "failure", "string")))),
                        ),
                    ),
                ),
            )
        val newInterpreter = interpreter.interpret(ast)
        assertEquals("success", newInterpreter.report.outputs.first())
    }

    @Test
    fun test005_testIfAndElseStatementIfFalse() {
        val ast =
            Scope(
                "file",
                Range(0, 0),
                listOf(
                    IfAndElseStatement(
                        Range(0, 0),
                        listOf(BooleanCondition(Range(0, 0), LiteralArgument(Range(0, 0), "false", "boolean"))),
                        Scope(
                            "ifScope",
                            Range(0, 0),
                            listOf(Call(Range(0, 0), "println", listOf(LiteralArgument(Range(0, 0), "success", "string")))),
                        ),
                        Scope(
                            "elseScope",
                            Range(0, 0),
                            listOf(Call(Range(0, 0), "println", listOf(LiteralArgument(Range(0, 0), "failure", "string")))),
                        ),
                    ),
                ),
            )
        val newInterpreter = interpreter.interpret(ast)
        assertEquals("failure", newInterpreter.report.outputs.first())
    }
}
