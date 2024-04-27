import ast.BooleanCondition
import ast.IfAndElseStatement
import ast.LiteralArgument
import ast.Range
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class ParserIfElseConditionalTest {
    @Test
    fun `test001 test simple boolean if and else with empty scopes`() {
        val code = "if(a){} else{}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 13),
                listOf(
                    IfAndElseStatement(
                        Range(0, 13),
                        listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                        Scope("program", Range(5, 5), emptyList()),
                        Scope("program", Range(12, 12), emptyList()),
                    ),
                ),
            )

        Assertions.assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test002 test simple boolean if and else with empty scopes but spaces inside else`() {
        val code = "if(a){} else{  }"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 15),
                listOf(
                    IfAndElseStatement(
                        Range(0, 15),
                        listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                        Scope("program", Range(5, 5), emptyList()),
                        Scope("program", Range(12, 12), emptyList()),
                    ),
                ),
            )

        Assertions.assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test003 test simple boolean if and else with scope in both`() {
        val code = "if(a){let b: number = 1;}else{let b: number = 1;}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 48),
                listOf(
                    IfAndElseStatement(
                        Range(0, 48),
                        listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                        Scope(
                            "program",
                            Range(6, 23),
                            listOf(
                                VariableDeclaration(
                                    Range(10, 10),
                                    "b",
                                    "number",
                                    LiteralArgument(Range(22, 22), "1", "number"),
                                ),
                            ),
                        ),
                        Scope(
                            "program",
                            Range(30, 47),
                            listOf(
                                VariableDeclaration(
                                    Range(34, 34),
                                    "b",
                                    "number",
                                    LiteralArgument(Range(46, 46), "1", "number"),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        Assertions.assertEquals(Result.success(expected), ast)
    }
}
