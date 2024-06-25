import ast.BooleanCondition
import ast.Call
import ast.IfStatement
import ast.LiteralArgument
import ast.MethodResult
import ast.Range
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class ParserIfConditionalTest {
    @Test
    fun `test001 test simple boolean if with empty scope`() {
        val code = "if(a){}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        // If it's empty, the range of the if scope is the one of the first bracket.
        val expected =
            Scope(
                "program",
                Range(0, 6),
                listOf(
                    IfStatement(
                        Range(0, 6),
                        listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                        Scope("program", Range(5, 5), emptyList()),
                    ),
                ),
            )

        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test002 test simple boolean if with empty scope but lots of spaces inside`() {
        val code = "if(a){   }"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 9),
                listOf(
                    IfStatement(
                        Range(0, 9),
                        listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                        Scope("program", Range(5, 5), emptyList()),
                    ),
                ),
            )

        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test003 test if with a variable declaration inside`() {
        val code = "if(a){let b: number = 1;}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 24),
                listOf(
                    IfStatement(
                        Range(0, 24),
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
                    ),
                ),
            )
        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test004 test if with a println of a text`() {
        val code = "if (a) {println(\"Hello\");}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
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
                        ),
                    ),
                ),
            )
        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test005 test simple consecutive ifs with empty scope`() {
        val code = "if(a){}if(a){}if(a){}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        // If it's empty, the range of the if scope is the one of the first bracket.
        val expected =
            Scope(
                "program",
                Range(0, 20),
                listOf(
                    IfStatement(
                        Range(0, 6),
                        listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                        Scope("program", Range(5, 5), emptyList()),
                    ),
                    IfStatement(
                        Range(7, 13),
                        listOf(BooleanCondition(Range(10, 10), VariableArgument(Range(10, 10), "a"))),
                        Scope("program", Range(12, 12), emptyList()),
                    ),
                    IfStatement(
                        Range(14, 20),
                        listOf(BooleanCondition(Range(17, 17), VariableArgument(Range(17, 17), "a"))),
                        Scope("program", Range(19, 19), emptyList()),
                    ),
                ),
            )

        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test006 test if after variable declaration`(){
        val code = "let a: number = 1; if(a){}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 25),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "number",
                        LiteralArgument(Range(16, 16), "1", "number"),
                    ),
                    IfStatement(
                        Range(19, 25),
                        listOf(BooleanCondition(Range(22, 22), VariableArgument(Range(22, 22), "a"))),
                        Scope("program", Range(24, 24), emptyList()),
                    ),
                ),
            )
        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test007 test if with println inside after a variable declaration`(){
        val code = "let a: number = 1; if(a){println(a);}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 36),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "number",
                        LiteralArgument(Range(16, 16), "1", "number"),
                    ),
                    IfStatement(
                        Range(19, 36),
                        listOf(BooleanCondition(Range(22, 22), VariableArgument(Range(22, 22), "a"))),
                        Scope(
                            "program",
                            Range(25, 35),
                            listOf(
                                MethodResult(
                                    Range(25, 31),
                                    Call(Range(33, 33), "println", listOf(VariableArgument(Range(33, 33), "a"))),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        println(ast);
        assertEquals(Result.success(expected), ast)
    }

    @Test
    fun `test008 test if with println inside after a variable boolean declaration`(){
        val code = "let a: boolean = true; if(a){println(a);}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 40),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "boolean",
                        LiteralArgument(Range(17, 20), "true", "boolean"),
                    ),
                    IfStatement(
                        Range(23, 40),
                        listOf(BooleanCondition(Range(26, 26), VariableArgument(Range(26, 26), "a"))),
                        Scope(
                            "program",
                            Range(29, 39),
                            listOf(
                                MethodResult(
                                    Range(29, 35),
                                    Call(Range(37, 37), "println", listOf(VariableArgument(Range(37, 37), "a"))),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        println(ast);
        assertEquals(Result.success(expected), ast)
    }
}
