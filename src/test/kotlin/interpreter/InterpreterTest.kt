package interpreter

import ast.*
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser
import kotlin.test.assertNotNull

class InterpreterTest {

    private val interpreter = Interpreter()
    private val defaultInterpreter = DefaultInterpreter()

    @Test
    fun testDeclarationSentence() {
        val ast = AbstractSyntaxTree(listOf(DeclareNode(PrintScriptType.STRING, "name")))
        val report = interpreter.interpret(ast)
        val expected = mapOf("name" to Literal(PrintScriptType.STRING, null))

        assertEquals(expected, interpreter.variables)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    //TODO: design handle reassign type of variables already declared.
    @Test
    fun testMultipleDeclarationSentences() {
        val ast = AbstractSyntaxTree(listOf(
            DeclareNode(PrintScriptType.STRING, "name"),
            DeclareNode(PrintScriptType.NUMBER, "year"),
            DeclareNode(PrintScriptType.NUMBER, "age"),
        ))
        val report = interpreter.interpret(ast)
        val expected = mapOf(
            "name" to Literal(PrintScriptType.STRING, null),
            "year" to Literal(PrintScriptType.NUMBER, null),
            "age" to Literal(PrintScriptType.NUMBER, null)
        )

        assertEquals(expected, interpreter.variables)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    @Test
    fun testAssignationSentence() {
        val ast = AbstractSyntaxTree(listOf(
            DeclareNode(PrintScriptType.NUMBER, "year"),
            AssignNode("year", LiteralNode(PrintScriptType.NUMBER, "2024"))
        ))
        val report = interpreter.interpret(ast)
        val expected = mapOf("year" to Literal(PrintScriptType.NUMBER, "2024"))

        assertEquals(expected, interpreter.variables)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    @Test
    fun testDeclareAndAssignSentence() {
        val ast = AbstractSyntaxTree(listOf(
            DeclareAndAssignNode(
                DeclareNode(PrintScriptType.NUMBER, "age"),
                LiteralNode(PrintScriptType.NUMBER, "23")
            )
        ))
        val report = interpreter.interpret(ast)
        val expected = mapOf("age" to Literal(PrintScriptType.NUMBER, "23"))

        assertEquals(expected, interpreter.variables)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    @Test
    fun testConcatenateStrings() {
        val ast = AbstractSyntaxTree(listOf(
            DeclareAndAssignNode(
                DeclareNode(PrintScriptType.STRING, "message"),
                AdditionNode(
                    LiteralNode(PrintScriptType.STRING, "Hello"),
                    LiteralNode(PrintScriptType.STRING, " World!")
                )
            )
        ))
        val report = interpreter.interpret(ast)
        val expected = mapOf("message" to Literal(PrintScriptType.STRING, "Hello World!"))

        assertEquals(expected, interpreter.variables)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    @Test
    fun testPrintLnSentence() {
        val ast = AbstractSyntaxTree(listOf(
            PrintNode(LiteralNode(PrintScriptType.STRING, "Happy 2024!"))
        ))
        val report = interpreter.interpret(ast)

        assertEquals(1, report.outputs.size)
        assertEquals("Happy 2024!", report.outputs.first())
        assertTrue(interpreter.variables.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    @Test
    fun testArithmeticSentences() {
        val ast = AbstractSyntaxTree(listOf(
            DeclareAndAssignNode(
                DeclareNode(PrintScriptType.NUMBER, "operationA"),
                AdditionNode(
                    AdditionNode(
                        LiteralNode(PrintScriptType.NUMBER, "9"),
                        LiteralNode(PrintScriptType.NUMBER, "21")
                    ),
                    LiteralNode(PrintScriptType.NUMBER, "100")
                )
            ),
            DeclareAndAssignNode(
                DeclareNode(PrintScriptType.NUMBER, "operationB"),
                SubtractionNode(
                    LiteralNode(PrintScriptType.NUMBER, "64"),
                    LiteralNode(PrintScriptType.NUMBER, "16")
                )
            ),
            DeclareAndAssignNode(
                DeclareNode(PrintScriptType.NUMBER, "operationC"),
                MultiplicationNode(
                    LiteralNode(PrintScriptType.NUMBER, "7"),
                    LiteralNode(PrintScriptType.NUMBER, "12"),
                )
            ),
            DeclareAndAssignNode(
                DeclareNode(PrintScriptType.NUMBER, "operationD"),
                DivisionNode(
                    LiteralNode(PrintScriptType.NUMBER, "256"),
                    LiteralNode(PrintScriptType.NUMBER, "64")
                )
            )
        ))
        val report = interpreter.interpret(ast)
        val expected = mapOf(
            "operationA" to Literal(PrintScriptType.NUMBER, "130.0"),
            "operationB" to Literal(PrintScriptType.NUMBER, "48.0"),
            "operationC" to Literal(PrintScriptType.NUMBER, "84.0"),
            "operationD" to Literal(PrintScriptType.NUMBER, "4.0")
        )

        assertEquals(expected, interpreter.variables)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    @Test
    fun testComplexArithmeticSentence() {
        //let operation: number = 8 + 4 * 7 - (100 / 5 - 10) * 4;  -> -4.0
        val ast = AbstractSyntaxTree(listOf(
            DeclareAndAssignNode(
                DeclareNode(PrintScriptType.NUMBER, "operation"),
                SubtractionNode(
                    AdditionNode(
                        LiteralNode(PrintScriptType.NUMBER, "8"),
                        MultiplicationNode(
                            LiteralNode(PrintScriptType.NUMBER, "4"),
                            LiteralNode(PrintScriptType.NUMBER, "7")
                        )
                    ),
                    MultiplicationNode(
                        SubtractionNode(
                            DivisionNode(
                                LiteralNode(PrintScriptType.NUMBER, "100"),
                                LiteralNode(PrintScriptType.NUMBER, "5")
                            ),
                            LiteralNode(PrintScriptType.NUMBER, "10")
                        ),
                        LiteralNode(PrintScriptType.NUMBER, "4")
                    )
                )
            )
        ))
        val report = interpreter.interpret(ast)
        val expected = mapOf("operation" to Literal(PrintScriptType.NUMBER, "-4.0"))

        assertEquals(expected, interpreter.variables)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }

    @Test
    fun testPipeline() {
        val code = "let sum: int = ((3 + 5) * (2 + 4));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val report = defaultInterpreter.interpret(ast)
        val target = defaultInterpreter.variables["sum"]

        assertNotNull(target)
        assertEquals("int", target.type)
        assertEquals("48", target.value)
        assertEquals(1, defaultInterpreter.variables.size)
        assertTrue(report.outputs.isEmpty())
        assertTrue(report.errors.isEmpty())
    }
}
