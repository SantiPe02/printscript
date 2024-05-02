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

class ParserVariableDeclarationTest {
    @Test
    fun test001_testSimpleLiteralVariableDeclarationNumber() {
        val code = "let a: number = 5;" // range 14, including spaces...
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 17),
                    listOf(
                        VariableDeclaration(Range(4, 4), "a", "number", LiteralArgument(Range(16, 16), "5", "number")),
                    ),
                ),
            )

        Assertions.assertEquals(expected, ast)
    }

    @Test
    fun test002_testSimpleLiteralVariableDeclarationBool() {
        val code = "let a: boolean = true;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 21),
                    listOf(
                        VariableDeclaration(Range(4, 4), "a", "boolean", LiteralArgument(Range(17, 20), "true", "boolean")),
                    ),
                ),
            )

        Assertions.assertEquals(expected, ast)
    }

    @Test
    fun test003_testSimpleLiteralVariableDeclarationNumber() {
        val code = "let a: number = 3.5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 19),
                    listOf(
                        VariableDeclaration(Range(4, 4), "a", "number", LiteralArgument(Range(16, 18), "3.5", "number")),
                    ),
                ),
            )

        Assertions.assertEquals(expected, ast)
    }

    @Test
    fun test004_testSimpleLiteralVariableDeclarationStringOfOneWord() {
        val code = "let a: string = \"Juan\";"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 22),
                    listOf(
                        VariableDeclaration(
                            Range(4, 4),
                            "a",
                            "string",
                            LiteralArgument(Range(16, 21), "Juan", "string"),
                        ),
                    ),
                ),
            )

        Assertions.assertEquals(expected, ast)
    }

    @Test
    fun test005_testConsecutiveLiteralDeclarations() {
        val code = "let a: string = \"Juan\"; let b: number = 5; let c: boolean = true;"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 64),
                    listOf(
                        VariableDeclaration(
                            Range(4, 4),
                            "a",
                            "string",
                            LiteralArgument(Range(16, 21), "Juan", "string"),
                        ),
                        VariableDeclaration(Range(28, 28), "b", "number", LiteralArgument(Range(40, 40), "5", "number")),
                        VariableDeclaration(
                            Range(47, 47),
                            "c",
                            "boolean",
                            LiteralArgument(Range(60, 63), "true", "boolean"),
                        ),
                    ),
                ),
            )

        Assertions.assertEquals(expected, ast)
    }

    @Test
    fun test006_testDeclarationWithVariableArgument() {
        val code = "let a: number = b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 17), // Adjusted range
                    listOf(
                        VariableDeclaration(Range(4, 4), "a", "number", VariableArgument(Range(16, 16), "b")),
                    ),
                ),
            )

        Assertions.assertEquals(expected, ast)
    }

    // see, it doesn't check if the type of the variable is correct
    // but I don't think that is something for the parser to check
    @Test
    fun test007_testDeclarationsWithLiteralAndVariableArguments() {
        val code = "let a: number = 5; let b: string = a;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 36),
                    listOf(
                        VariableDeclaration(Range(4, 4), "a", "number", LiteralArgument(Range(16, 16), "5", "number")),
                        VariableDeclaration(Range(23, 23), "b", "string", VariableArgument(Range(35, 35), "a")),
                    ),
                ),
            )

        Assertions.assertEquals(expected, ast)
    }
}
