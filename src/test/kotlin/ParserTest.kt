import ast.*
import lexer.LexerImpl
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser
import parser.ParserCommons


class ParserTest {

    @Test
    fun testSimpleLiteralVariableDeclarationInt(){
        val code = "let a: int = 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "a", "int", LiteralArgument(Range(0, 0), "5", "int"))
            ))

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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "a", "boolean", LiteralArgument(Range(0, 0), "true", "boolean"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }


    // Not supported yet, I believe
    @Test
    fun testSimpleLiteralVariableDeclarationFloat(){
        val code = "let a: float = 3.5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "a", "float", LiteralArgument(Range(0, 0), "3.5", "float"))
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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "a", "string", LiteralArgument(Range(0, 0), "\"Juan\"", "string"))
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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "a", "string", LiteralArgument(Range(0, 0), "\"Juan\"", "string")),
                VariableDeclaration( Range(0, 0), "b", "int", LiteralArgument(Range(0, 0), "5", "int")),
                VariableDeclaration( Range(0, 0), "c", "boolean", LiteralArgument(Range(0, 0), "true", "boolean"))
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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "a", "int", VariableArgument(Range(0, 0), "b"))
            ))

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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "a", "int", LiteralArgument(Range(0, 0), "5", "int")),
                VariableDeclaration( Range(0, 0), "b", "string", VariableArgument(Range(0, 0), "a"))
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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int", MethodResult(Range(0, 0), Call((Range(0, 0)), "+", listOf(LiteralArgument(Range(0, 0), "3", "int"), LiteralArgument(Range(0, 0), "5", "int")))))));

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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int", MethodResult(Range(0, 0), Call((Range(0, 0)), "+", listOf(VariableArgument(Range(0, 0), "a"), LiteralArgument(Range(0, 0), "5", "int")))))));


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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int",
                    MethodResult(Range(0, 0), Call((Range(0, 0)), "+",
                        listOf( LiteralArgument(Range(0, 0), "3", "int"),
                                MethodResult(Range(0, 0), Call((Range(0, 0)), "*",
                                    listOf( LiteralArgument(Range(0, 0), "5", "int"),
                                            LiteralArgument(Range(0, 0), "2", "int"))))))))));

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
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int",
                    MethodResult(Range(0, 0), Call((Range(0, 0)), "+",
                        listOf(MethodResult(Range(0, 0), Call((Range(0, 0)), "*",
                            listOf( LiteralArgument(Range(0, 0), "3", "int"),
                                    LiteralArgument(Range(0, 0), "5", "int")))),
                            LiteralArgument(Range(0, 0), "2", "int")))))));
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testVeryComplexMethodDeclarationWithOutParentheses(){
        val code = "let sum:int = 3 * 5 + 2 - 4 / 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf(
                VariableDeclaration( Range(0, 0), "sum", "int", MethodResult(Range(0, 0), Call((Range(0, 0)), "+", listOf(MethodResult(Range(0, 0), Call((Range(0, 0)), "*", listOf(LiteralArgument(Range(0, 0), "3", "int"), LiteralArgument(Range(0, 0), "5", "int")))), MethodResult(Range(0, 0), Call((Range(0, 0)), "-", listOf(LiteralArgument(Range(0, 0), "2", "int"), MethodResult(Range(0, 0), Call((Range(0, 0)), "/", listOf(LiteralArgument(Range(0, 0), "4", "int"), LiteralArgument(Range(0, 0), "2", "int")))))))))))));
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun testSearchForClosingCharacter(){
        val code = "((()))))"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens,"(", 0)
        val expected = 6
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
                        listOf(MethodResult(Range(0, 0), Call((Range(0, 0)), "+",
                            listOf( LiteralArgument(Range(0, 0), "3", "int"),
                                    LiteralArgument(Range(0, 0), "5", "int")))),
                            LiteralArgument(Range(0, 0), "2", "int")))))));

        kotlin.test.assertEquals(expected, ast)
    }
}