import ast.*
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class ParserIfConditionalTest {


    @Test
    fun `test001 test simple boolean if with empty scope`(){
        val code = "if(a){}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        // If it's empty, the range of the if scope is the one of the first bracket.
        val expected = Scope(
            "program",
            Range(0, 6),
            listOf(
                IfStatement(
                    Range(0, 6),
                    listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                    Scope("program", Range(5, 5), emptyList())
            )
        ))

        assertEquals(Result.success(expected), ast)
    }
    @Test
    fun `test002 test simple boolean if with empty scope but lots of spaces inside`(){
        val code = "if(a){   }"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 9),
            listOf(
                IfStatement(
                    Range(0, 9),
                    listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                    Scope("program", Range(5, 5), emptyList())
                )
            ))

        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test003 test if with a println of a text`(){
        val code = "if (a) {println(\"Hello\");}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 25),
            listOf(
                IfStatement(
                    Range(0, 25),
                    listOf(BooleanCondition(Range(4, 4), VariableArgument(Range(4, 4), "a"))),
                    Scope(
                        "program",
                        Range(8, 24),
                        listOf(
                            MethodResult(
                                Range(8, 14),
                                Call(Range(16, 22), "println", listOf(LiteralArgument(Range(16, 22), "Hello", "string"))),
                            ),
                        ),
                    )
                )
            )
        )
        assertEquals(Result.success(expected), ast)
    }

}