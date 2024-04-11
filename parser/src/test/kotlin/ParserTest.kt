import ast.AssignmentStatement
import ast.Call
import ast.DeclarationStatement
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
import parser.ParserCommons

class ParserTest {
    @Test
    fun test001_testSimpleLiteralVariableDeclarationInt() {
        val code = "let a: int = 5;" // range 14, including spaces...
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 14),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "int", LiteralArgument(Range(13, 13), "5", "int")),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test002_testSimpleLiteralVariableDeclarationBool() {
        val code = "let a: boolean = true;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 21),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "boolean", LiteralArgument(Range(17, 20), "true", "boolean")),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test003_testSimpleLiteralVariableDeclarationFloat() {
        val code = "let a: float = 3.5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 18),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "float", LiteralArgument(Range(15, 17), "3.5", "float")),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test004_testSimpleLiteralVariableDeclarationStringOfOneWord() {
        val code = "let a: string = \"Juan\";"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 22),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "string",
                        LiteralArgument(Range(16, 21), "\"Juan\"", "string")
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test005_testConsecutiveLiteralDeclarations() {
        val code = "let a: string = \"Juan\"; let b: int = 5; let c: boolean = true;"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 61),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "string",
                        LiteralArgument(Range(16, 21), "\"Juan\"", "string")
                    ),
                    VariableDeclaration(Range(28, 28), "b", "int", LiteralArgument(Range(37, 37), "5", "int")),
                    VariableDeclaration(
                        Range(44, 44),
                        "c",
                        "boolean",
                        LiteralArgument(Range(57, 60), "true", "boolean")
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test006_testDeclarationWithVariableArgument() {
        val code = "let a: int = b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 14), // Adjusted range
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "int", VariableArgument(Range(13, 13), "b")),
                ),
            )

        assertEquals(expected, ast)
    }

    // see, it doesn't check if the type of the variable is correct
    // but I don't think that is something for the parser to check
    @Test
    fun test007_testDeclarationsWithLiteralAndVariableArguments() {
        val code = "let a: int = 5; let b: string = a;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 33),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "int", LiteralArgument(Range(13, 13), "5", "int")),
                    VariableDeclaration(Range(20, 20), "b", "string", VariableArgument(Range(32, 32), "a")),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test008_testSumMethodDeclaration() {
        val code = "let sum: int = 3 + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 20),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(17, 17),
                            Call(
                                (Range(15, 19)),
                                "+",
                                listOf(
                                    LiteralArgument(Range(15, 15), "3", "int"),
                                    LiteralArgument(Range(19, 19), "5", "int")
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test009_testSumMethodDeclarationWithVariable() {
        val code = "let sum: int = a + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 20),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(17, 17),
                            Call(
                                (Range(15, 19)),
                                "+",
                                listOf(
                                    VariableArgument(Range(15, 15), "a"),
                                    LiteralArgument(Range(19, 19), "5", "int")
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test010_testSumAndMultiplicationMethodDeclaration() {
        val code = "let sum: int = 3 + 5 * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 24),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(17, 17),
                            Call(
                                (Range(15, 23)),
                                "+",
                                listOf(
                                    LiteralArgument(Range(15, 15), "3", "int"),
                                    MethodResult(
                                        Range(21, 21),
                                        Call(
                                            (Range(19, 23)),
                                            "*",
                                            listOf(
                                                LiteralArgument(Range(19, 19), "5", "int"),
                                                LiteralArgument(Range(23, 23), "2", "int"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test011_testMultiplicationAndSumMethodDeclaration() {
        val code = "let sum: int = 3 * 5 + 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 24),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(21, 21),
                            Call(
                                (Range(15, 23)),
                                "+",
                                listOf(
                                    MethodResult(
                                        Range(17, 17),
                                        Call(
                                            (Range(15, 19)),
                                            "*",
                                            listOf(
                                                LiteralArgument(Range(15, 15), "3", "int"),
                                                LiteralArgument(Range(19, 19), "5", "int"),
                                            ),
                                        ),
                                    ),
                                    LiteralArgument(Range(23, 23), "2", "int"),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, ast)
    }

    @Test
    fun test012_testVeryComplexMethodDeclarationWithOutParentheses() {
        val code = "let sum: int = 3 * 5 + 2 - 4 / 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 32),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(21, 21),
                            Call(
                                (Range(15, 31)),
                                "+",
                                listOf(
                                    MethodResult(
                                        Range(17, 17),
                                        Call(
                                            (Range(15, 19)),
                                            "*",
                                            listOf(
                                                LiteralArgument(Range(15, 15), "3", "int"),
                                                LiteralArgument(Range(19, 19), "5", "int"),
                                            ),
                                        ),
                                    ),
                                    MethodResult(
                                        Range(25, 25),
                                        Call(
                                            (Range(23, 31)),
                                            "-",
                                            listOf(
                                                LiteralArgument(Range(23, 23), "2", "int"),
                                                MethodResult(
                                                    Range(29, 29),
                                                    Call(
                                                        (Range(27, 31)),
                                                        "/",
                                                        listOf(
                                                            LiteralArgument(Range(27, 27), "4", "int"),
                                                            LiteralArgument(Range(31, 31), "2", "int"),
                                                        ),
                                                    ),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, ast)
    }

    @Test
    fun test013_testSearchForClosingCharacter() {
        val code = "((()))))"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 0)
        val expected = 5
        assertEquals(expected, gotChar)
    }

    @Test
    fun test014_testMethodDeclarationWithParentheses() {
        val code = "let sum: int = (3 + 5) * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 26),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(23, 23),
                            Call(
                                Range(15, 25),
                                "*",
                                listOf(
                                    MethodResult(
                                        Range(18, 18),
                                        Call(
                                            (Range(16, 20)),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(16, 16), "3", "int"),
                                                LiteralArgument(Range(20, 20), "5", "int"),
                                            ),
                                        ),
                                    ),
                                    LiteralArgument(Range(25, 25), "2", "int"),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test015_testMethodDeclarationMultOfTwoParentheses() {
        val code = "let sum: int = (3 + 5) * (2 + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 32),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(23, 23),
                            Call(
                                Range(15, 31),
                                "*",
                                listOf(
                                    MethodResult(
                                        Range(18, 18),
                                        Call(
                                            (Range(16, 20)),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(16, 16), "3", "int"),
                                                LiteralArgument(Range(20, 20), "5", "int"),
                                            ),
                                        ),
                                    ),
                                    MethodResult(
                                        Range(28, 28),
                                        Call(
                                            Range(26, 30),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(26, 26), "2", "int"),
                                                LiteralArgument(Range(30, 30), "4", "int"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    // solucionar el tema de que en realidad si usa parentesis non meterlos como method result. El parentesis no es un operator.
    @Test
    fun test016_testParenthesesInsideParentheses() {
        val code = "let sum: int = ((3 + 5) * (2 + 4));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 34),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(24, 24),
                            Call(
                                Range(16, 32),
                                "*",
                                listOf(
                                    MethodResult(
                                        Range(19, 19),
                                        Call(
                                            (Range(17, 21)),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(17, 17), "3", "int"),
                                                LiteralArgument(Range(21, 21), "5", "int"),
                                            ),
                                        ),
                                    ),
                                    MethodResult(
                                        Range(29, 29),
                                        Call(
                                            Range(27, 31),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(27, 27), "2", "int"),
                                                LiteralArgument(Range(31, 31), "4", "int"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    // falta resolver el tema de las comas. No son operadores, son SEPARADORES.
    // Quiza, en el getFinalArgumentsOfMethodResult fijarse en vez del tamaño, si hay operadores.
    @Test
    fun test017_testMethodDeclarationOfAMethodCall() {
        val code = "let test: int = sum(3, 5);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 25),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "int",
                        MethodResult(
                            Range(16, 18),
                            Call(
                                Range(20, 23),
                                "sum",
                                listOf(
                                    LiteralArgument(Range(20, 20), "3", "int"),
                                    LiteralArgument(Range(23, 23), "5", "int"),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test018_testMethodDeclarationWithOperationInside() {
        val code = "let test: int = sum(3, 5 + 2);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 29),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "int",
                        MethodResult(
                            Range(16, 18),
                            Call(
                                Range(20, 27),
                                "sum",
                                listOf(
                                    LiteralArgument(Range(20, 20), "3", "int"),
                                    MethodResult(
                                        Range(25, 25),
                                        Call(
                                            Range(23, 27),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(23, 23), "5", "int"),
                                                LiteralArgument(Range(27, 27), "2", "int"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test019_testMethodInsideMethod() {
        val code = "let test: int = sum(3, sum(5, 2));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 33),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "int",
                        MethodResult(
                            Range(16, 18),
                            Call(
                                Range(20, 31),
                                "sum",
                                listOf(
                                    LiteralArgument(Range(20, 20), "3", "int"),
                                    MethodResult(
                                        Range(23, 25),
                                        Call(
                                            Range(27, 30),
                                            "sum",
                                            listOf(
                                                LiteralArgument(Range(27, 27), "5", "int"),
                                                LiteralArgument(Range(30, 30), "2", "int"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    // lo que pasa en sum(3, sum(5, 2)) es que ya de una te toma tres metodos: 3, sum(5 y 2)
    @Test
    fun test020_testSearchForClosingBracketsMethod() {
        val code = "let test: int = sum(3, sum(5, 2));"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 6)
        val expected = 15
        assertEquals(expected, gotChar)
    }

    @Test
    fun test021_testSearchForClosingBracketsMethodPt2() {
        val code = "let test: int = sum(3, sum(5, 2));"

        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 10)
        val expected = 14
        assertEquals(expected, gotChar)
    }

    @Test
    fun test022_testComplexEcuationWithParenthesesButParenthesesAreNotIncludedInTheAST() {
        val code = "let test: int = 3 + (5 * 2) + 4;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 31),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "int",
                        MethodResult(
                            Range(18, 18),
                            Call(
                                Range(16, 30),
                                "+",
                                listOf(
                                    LiteralArgument(Range(16, 16), "3", "int"),
                                    MethodResult(
                                        Range(28, 28),
                                        Call(
                                            Range(20, 30),
                                            "+",
                                            listOf(
                                                MethodResult(
                                                    Range(23, 23),
                                                    Call(
                                                        Range(21, 25),
                                                        "*",
                                                        listOf(
                                                            LiteralArgument(Range(21, 21), "5", "int"),
                                                            LiteralArgument(Range(25, 25), "2", "int"),
                                                        ),
                                                    ),
                                                ),
                                                LiteralArgument(Range(30, 30), "4", "int"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun test023_expresionStatementTest() {
        val code = "let a: int;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 10),
                listOf(DeclarationStatement(Range(4, 4), "a", "int")),
            )
        assertEquals(ast, expected)
    }

    @Test
    fun test024_variableDeclarationInTwoDifferentParts() {
        val code = "let a: int; a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 18),
                listOf(
                    DeclarationStatement(Range(4, 4), "a", "int"),
                    AssignmentStatement(Range(12, 12), "a", LiteralArgument(Range(16, 17), "54", "int")),
                ),
            )

        assertEquals(ast, expected)
    }

    @Test
    fun test025_assignementStatement() {
        val code = "a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 6),
                listOf(AssignmentStatement(Range(0, 0), "a", LiteralArgument(Range(4, 5), "54", "int"))),
            )

        assertEquals(ast, expected)
    }

    @Test
    fun test026_assignementStatementArgumentIsLiteralAndCorrect() {
        val code = "a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val first = ast.body.first()
        if (first is AssignmentStatement) {
            assertEquals(first.value, LiteralArgument(Range(4, 5), "54", "int"))
        }
    }

    @Test
    fun test027_testSumOfTwoVariableArguments() {
        val code = "let sum: int = a + b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 20),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "int",
                        MethodResult(
                            Range(17, 17),
                            Call(
                                (Range(15, 19)),
                                "+",
                                listOf(
                                    VariableArgument(Range(15, 15), "a"),
                                    VariableArgument(Range(19, 19), "b"),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, ast)
    }

    @Test
    fun test028_testSumOfTwoVariableArguments_withFirstVariableArgBeingAlsoTheIdentifier() {
        val code = "let a: int = a + b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 18),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "int",
                        MethodResult(
                            Range(15, 15),
                            Call(
                                (Range(13, 17)),
                                "+",
                                listOf(
                                    VariableArgument(Range(13, 13), "a"),
                                    VariableArgument(Range(17, 17), "b"),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, ast)
    }

    @Test
    fun test029_testIsolatedMethodCall() {
        val code = "println(34);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 11),
                listOf(
                    MethodResult(
                        Range(0, 6),
                        Call(Range(8, 9), "println", listOf(LiteralArgument(Range(8, 9), "34", "int")))
                    )
                ),
            )
        assertEquals(expected, ast)
    }

    @Test
    fun test030_testIsolatedMethodCallWithInsideMethod() {
        val code = "println(34 + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 15),
                listOf(
                    MethodResult(
                        Range(0, 6),
                        Call(
                            Range(8, 13),
                            "println",
                            listOf(
                                MethodResult(
                                    Range(11, 11),
                                    Call(
                                        Range(8, 13),
                                        "+",
                                        listOf(
                                            LiteralArgument(Range(8, 9), "34", "int"),
                                            LiteralArgument(Range(13, 13), "4", "int")
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(expected, ast)
    }

    @Test
    fun test031_testEmptyMethodCall() {
        val code = "object();"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        val expected =
            Scope("program", Range(0, 8), listOf(MethodResult(Range(0, 7), Call(Range(0, 7), "object", listOf()))))
        assertEquals(expected, ast)
    }

    @Test
    fun test032_testEmptyMethodCallWithVariableDeclaration() {
        val code = "let a: String = object();"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 24),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "String",
                        MethodResult(Range(16, 23), Call(Range(16, 23), "object", listOf()))
                    ),
                ),
            )
        assertEquals(ast, expected)
    }

    @Test
    fun test032_testVariableDeclarationWithAnObjectThatAintStringNorNumberNorBool() {
        val code = "let a: object = object();"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Scope(
                "program",
                Range(0, 24),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "object",
                        MethodResult(Range(16, 23), Call(Range(16, 23), "object", listOf()))
                    ),
                ),
            )
        assertEquals(ast, expected)
    }

    @Test
    fun test033_testPrintlnWithSumOfStringAndInt() {
        val code = "println(\"Hello\" + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        val expected = Scope(
            "program",
            Range(0, 20),
            listOf(
                MethodResult(
                    Range(0, 6),
                    Call(
                        Range(8, 18),
                        "println",
                        listOf(
                            MethodResult(
                                Range(16, 16),
                                Call(
                                    Range(8, 18),
                                    "+",
                                    listOf(
                                        LiteralArgument(Range(8, 14), "\"Hello\"", "string"),
                                        LiteralArgument(Range(18, 18), "4", "int"),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )

        assertEquals(ast, expected)
    }

    @Test
    fun test034_testWithStringsOFTripleQuote() {
        val code = """println("Hello" + 4);"""
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        val expected = Scope(
            "program",
            Range(0, 20),
            listOf(
                MethodResult(
                    Range(0, 6),
                    Call(
                        Range(8, 18),
                        "println",
                        listOf(
                            MethodResult(
                                Range(16, 16),
                                Call(
                                    Range(8, 18),
                                    "+",
                                    listOf(
                                        LiteralArgument(Range(8, 14), "\"Hello\"", "string"),
                                        LiteralArgument(Range(18, 18), "4", "int"),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )

        assertEquals(ast, expected)
    }

}
