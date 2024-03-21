import ast.*
import lexer.LexerImpl
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser
import parser.ParserCommons


class ParserTest {

    @Test
    fun testSimpleLiteralVariableDeclarationInt(){
        val code = "let a: int = 5;" // range 14, including spaces...
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 14),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "int", LiteralArgument(Range(13, 13), "5", "int"))
            ))

        println(expected.range.start)
        println(expected.range.end)
        kotlin.test.assertEquals(expected, ast)
    }
    @Test
    fun testSimpleLiteralVariableDeclarationBool(){
        val code = "let a: boolean = true;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 21),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "boolean", LiteralArgument(Range(17, 20), "true", "boolean"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }


    @Test
    fun testSimpleLiteralVariableDeclarationFloat(){
        val code = "let a: float = 3.5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 18),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "float", LiteralArgument(Range(15, 17), "3.5", "float"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testSimpleLiteralVariableDeclarationStringOfOneWord(){
        val code = "let a: string = \"Juan\";"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 22),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "string", LiteralArgument(Range(16, 21), "\"Juan\"", "string"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testConsecutiveLiteralDeclarations(){
        val code = "let a: string = \"Juan\"; let b: int = 5; let c: boolean = true;"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 61),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "string", LiteralArgument(Range(16, 21), "\"Juan\"", "string")),
                VariableDeclaration( Range(28, 28), "b", "int", LiteralArgument(Range(37, 37), "5", "int")),
                VariableDeclaration( Range(44, 44), "c", "boolean", LiteralArgument(Range(57, 60), "true", "boolean"))
            ))

        println(ast.body)
        println(expected.body)
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testDeclarationWithVariableArgument(){
        val code = "let a: int = b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 14), // Adjusted range
            listOf(
                VariableDeclaration(Range(4, 4), "a", "int", VariableArgument(Range(13, 13), "b")))
        )

        kotlin.test.assertEquals(expected, ast)
    }


    // see, it doesn't check if the type of the variable is correct
    // but I don't think that is something for the parser to check
    @Test
    fun testDeclarationsWithLiteralAndVariableArguments(){
        val code = "let a: int = 5; let b: string = a;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 33),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "int", LiteralArgument(Range(13, 13), "5", "int")),
                VariableDeclaration( Range(20, 20), "b", "string", VariableArgument(Range(32, 32), "a"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testSumMethodDeclaration(){
        val code = "let sum: int = 3 + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 20),
            listOf(
                VariableDeclaration(Range(4, 6), "sum", "int", MethodResult(Range(17, 17), Call((Range(15, 19)), "+", listOf(LiteralArgument(Range(15, 15), "3", "int"), LiteralArgument(Range(19, 19), "5", "int")))))));

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testSumMethodDeclarationWithVariable(){
        val code = "let sum: int = a + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 20),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int", MethodResult(Range(17, 17), Call((Range(15, 19)), "+", listOf(VariableArgument(Range(15, 15), "a"), LiteralArgument(Range(19, 19), "5", "int")))))));


        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testSumAndMultiplicationMethodDeclaration(){
        val code = "let sum: int = 3 + 5 * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 24),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(17, 17), Call((Range(15, 23)), "+",
                        listOf( LiteralArgument(Range(15, 15), "3", "int"),
                                MethodResult(Range(21, 21), Call((Range(19, 32)), "*",
                                    listOf( LiteralArgument(Range(19, 19), "5", "int"),
                                            LiteralArgument(Range(23, 23), "2", "int"))))))))));

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testMultiplicationAndSumMethodDeclaration(){
        val code = "let sum: int = 3 * 5 + 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 24),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(21, 21), Call((Range(15, 23)), "+",
                        listOf(MethodResult(Range(17, 17), Call((Range(15, 19)), "*",
                            listOf( LiteralArgument(Range(15, 15), "3", "int"),
                                    LiteralArgument(Range(19, 19), "5", "int")))),
                            LiteralArgument(Range(23, 23), "2", "int")))))));
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testVeryComplexMethodDeclarationWithOutParentheses(){
        val code = "let sum: int = 3 * 5 + 2 - 4 / 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 32),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(21, 21), Call((Range(15, 31)), "+",
                        listOf(MethodResult(Range(17, 17), Call((Range(15, 19)), "*",
                            listOf( LiteralArgument(Range(15, 15), "3", "int"),
                                    LiteralArgument(Range(19, 19), "5", "int")))),
                            MethodResult(Range(25, 25), Call((Range(23, 31)), "-",
                                    listOf(LiteralArgument(Range(23, 23), "2", "int"),
                                        MethodResult(Range(29, 29), Call((Range(27, 31)), "/",
                                            listOf( LiteralArgument(Range(27, 27), "4", "int"),
                                                    LiteralArgument(Range(31, 31), "2", "int")))))))))))));
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testSearchForClosingCharacter(){
        val code = "((()))))"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens,"(", 0)
        val expected = 5
        kotlin.test.assertEquals(expected, gotChar)
    }

    @Test
    fun testMethodDeclarationWithParentheses(){
        val code = "let sum:int = (3 + 5) * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int",
                    MethodResult(Range(0, 0), Call((Range(0, 0)), "*",
                        listOf(MethodResult(Range(0, 0), Call(Range(0,0), "(",
                            listOf(MethodResult(Range(0,0),Call((Range(0, 0)), "+",
                                listOf( LiteralArgument(Range(0, 0), "3", "int"),
                                        LiteralArgument(Range(0, 0), "5", "int"))))))),
                            LiteralArgument(Range(0, 0), "2", "int")))))));

        kotlin.test.assertEquals(expected, ast)
    }
    @Test
    fun testMethodDeclarationMultOfTwoParentheses(){
        val code = "let sum:int = (3 + 5) * (2 + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int",
                    MethodResult(Range(0, 0), Call((Range(0, 0)), "*",
                        listOf(MethodResult(Range(0, 0), Call(Range(0,0), "(",
                            listOf(MethodResult(Range(0,0),Call((Range(0, 0)), "+",
                                listOf( LiteralArgument(Range(0, 0), "3", "int"),
                                        LiteralArgument(Range(0, 0), "5", "int"))))))),
                            MethodResult(Range(0, 0), Call(Range(0,0), "(",
                                listOf(MethodResult(Range(0,0),Call((Range(0, 0)), "+",
                                    listOf( LiteralArgument(Range(0, 0), "2", "int"),
                                            LiteralArgument(Range(0, 0), "4", "int")))))))))))));

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testParenthesesInsideParentheses(){
        val code = "let sum:int = ((3 + 5) * (2 + 4));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int",
                    MethodResult(Range(0, 0), Call(Range(0,0), "(",
                        listOf(MethodResult(Range(0, 0), Call((Range(0, 0)), "*",
                            listOf(MethodResult(Range(0, 0), Call(Range(0,0), "(",
                                listOf(MethodResult(Range(0,0),Call((Range(0, 0)), "+",
                                    listOf( LiteralArgument(Range(0, 0), "3", "int"),
                                            LiteralArgument(Range(0, 0), "5", "int"))))))),
                            MethodResult(Range(0, 0), Call(Range(0,0), "(",
                                listOf(MethodResult(Range(0,0),Call((Range(0, 0)), "+",
                                    listOf( LiteralArgument(Range(0, 0), "2", "int"),
                                            LiteralArgument(Range(0, 0), "4", "int"))))))))))))))));

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testMethodDeclarationOfAMethodCall(){
        val code = "let test:int = sum(3, 5);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "test", "int",
                    MethodResult(Range(0, 0), Call(Range(0,0), "sum",
                        listOf( LiteralArgument(Range(0, 0), "3", "int"),
                                LiteralArgument(Range(0, 0), "5", "int")))))));

        kotlin.test.assertEquals(expected, ast)
    }
}