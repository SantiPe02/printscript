package interpreter

import ast.BooleanCondition
import ast.Call
import ast.IfAndElseStatement
import ast.IfStatement
import ast.LiteralArgument
import ast.Range
import ast.Scope
import factory.InterpreterFactoryImpl
import factory.LexerFactoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class InterpreterTest {
    private val init = InterpreterFactoryImpl("1.0").create()
    private val interpreter = Interpreter()
    private val lexer = LexerFactoryImpl("1.1").create()

    @Test
    fun test01_completeTestOfVersion1OfPrintscriptWhenValidCodeWithExitExpectedOfValue6() {
        val code = """
     let a: number = 12;
     let b: number = 4;
     let c: number;
     c = 3;
     a = a / b + c;

     println("Result: " + a);"""

        val tokens = lexer.tokenize(code)
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

    @Test
    fun test006_testDeclareAConstVariable() {
        val code = """
     const a : number = 10;
     """

        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val newInterpreter = interpreter.interpret(it)

            assertEquals(Variable("number", "10", true), newInterpreter.variables["a"])
            assertTrue(newInterpreter.report.errors.isEmpty())
        }
    }

    @Test
    fun test007_testDeclareAConstVariableAndTryToChangeTheValueLaterGettingAnError() {
        val code = """
     const a : number = 10;
     a = 5;
     """

        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val newInterpreter = interpreter.interpret(it)

            assertEquals(Variable("number", "10", true), newInterpreter.variables["a"])
            assertFalse(newInterpreter.report.errors.isEmpty())
        }
    }
}
