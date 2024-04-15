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
    fun test001_testSimpleLiteralVariableDeclarationnumber() {
        val code = "let a: number = 5;" // range 14, including spaces...
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 17),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "number", LiteralArgument(Range(16, 16), "5", "number")),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test002_testSimpleLiteralVariableDeclarationBool() {
        val code = "let a: boolean = true;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 21),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "boolean", LiteralArgument(Range(17, 20), "true", "boolean")),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test003_testSimpleLiteralVariableDeclarationNumber() {
        val code = "let a: number = 3.5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 19),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "number", LiteralArgument(Range(16, 18), "3.5", "number")),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test004_testSimpleLiteralVariableDeclarationStringOfOneWord() {
        val code = "let a: string = \"Juan\";"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
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
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test005_testConsecutiveLiteralDeclarations() {
        val code = "let a: string = \"Juan\"; let b: number = 5; let c: boolean = true;"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
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
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test006_testDeclarationWithVariableArgument() {
        val code = "let a: number = b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 17), // Adjusted range
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "number", VariableArgument(Range(16, 16), "b")),
                ),
            ))

        assertEquals(expected, ast)
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
            Result.success(Scope(
                "program",
                Range(0, 36),
                listOf(
                    VariableDeclaration(Range(4, 4), "a", "number", LiteralArgument(Range(16, 16), "5", "number")),
                    VariableDeclaration(Range(23, 23), "b", "string", VariableArgument(Range(35, 35), "a")),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test008_testSumMethodDeclaration() {
        val code = "let sum: number = 3 + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 23),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(20, 20),
                            Call(
                                (Range(18, 22)),
                                "+",
                                listOf(
                                    LiteralArgument(Range(18, 18), "3", "number"),
                                    LiteralArgument(Range(22, 22), "5", "number"),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test009_testSumMethodDeclarationWithVariable() {
        val code = "let sum: number = a + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 23),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(20, 20),
                            Call(
                                (Range(18, 22)),
                                "+",
                                listOf(
                                    VariableArgument(Range(18, 18), "a"),
                                    LiteralArgument(Range(22, 22), "5", "number"),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test010_testSumAndMultiplicationMethodDeclaration() {
        val code = "let sum: number = 3 + 5 * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 27),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(20, 20),
                            Call(
                                (Range(18, 26)),
                                "+",
                                listOf(
                                    LiteralArgument(Range(18, 18), "3", "number"),
                                    MethodResult(
                                        Range(24, 24),
                                        Call(
                                            (Range(22, 26)),
                                            "*",
                                            listOf(
                                                LiteralArgument(Range(22, 22), "5", "number"),
                                                LiteralArgument(Range(26, 26), "2", "number"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test011_testMultiplicationAndSumMethodDeclaration() {
        val code = "let sum: number = 3 * 5 + 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 27),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(24, 24),
                            Call(
                                (Range(18, 26)),
                                "+",
                                listOf(
                                    MethodResult(
                                        Range(20, 20),
                                        Call(
                                            (Range(18, 22)),
                                            "*",
                                            listOf(
                                                LiteralArgument(Range(18, 18), "3", "number"),
                                                LiteralArgument(Range(22, 22), "5", "number"),
                                            ),
                                        ),
                                    ),
                                    LiteralArgument(Range(26, 26), "2", "number"),
                                ),
                            ),
                        ),
                    ),
                ),
            ))
        assertEquals(expected, ast)
    }

    @Test
    fun test012_testVeryComplexMethodDeclarationWithOutParentheses() {
        val code = "let sum: number = 3 * 5 + 2 - 4 / 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 35),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(24, 24),
                            Call(
                                (Range(18, 34)),
                                "+",
                                listOf(
                                    MethodResult(
                                        Range(20, 20),
                                        Call(
                                            (Range(18, 22)),
                                            "*",
                                            listOf(
                                                LiteralArgument(Range(18, 18), "3", "number"),
                                                LiteralArgument(Range(22, 22), "5", "number"),
                                            ),
                                        ),
                                    ),
                                    MethodResult(
                                        Range(28, 28),
                                        Call(
                                            (Range(26, 34)),
                                            "-",
                                            listOf(
                                                LiteralArgument(Range(26, 26), "2", "number"),
                                                MethodResult(
                                                    Range(32, 32),
                                                    Call(
                                                        (Range(30, 34)),
                                                        "/",
                                                        listOf(
                                                            LiteralArgument(Range(30, 30), "4", "number"),
                                                            LiteralArgument(Range(34, 34), "2", "number"),
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
            ))
        assertEquals(expected, ast)
    }

    @Test
    fun test013_testSearchForClosingCharacter() {
        val code = "((()))))"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 0)
        val expected = Result.success(5)
        assertEquals(expected, gotChar)
    }

    @Test
    fun test014_testMethodDeclarationWithParentheses() {
        val code = "let sum: number = (3 + 5) * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 29),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(26, 26),
                            Call(
                                Range(18, 28),
                                "*",
                                listOf(
                                    MethodResult(
                                        Range(21, 21),
                                        Call(
                                            (Range(19, 23)),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(19, 19), "3", "number"),
                                                LiteralArgument(Range(23, 23), "5", "number"),
                                            ),
                                        ),
                                    ),
                                    LiteralArgument(Range(28, 28), "2", "number"),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test015_testMethodDeclarationMultOfTwoParentheses() {
        val code = "let sum: number = (3 + 5) * (2 + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 35),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(26, 26),
                            Call(
                                Range(18, 34),
                                "*",
                                listOf(
                                    MethodResult(
                                        Range(21, 21),
                                        Call(
                                            (Range(19, 23)),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(19, 19), "3", "number"),
                                                LiteralArgument(Range(23, 23), "5", "number"),
                                            ),
                                        ),
                                    ),
                                    MethodResult(
                                        Range(31, 31),
                                        Call(
                                            Range(29, 33),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(29, 29), "2", "number"),
                                                LiteralArgument(Range(33, 33), "4", "number"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    // solucionar el tema de que en realidad si usa parentesis non meterlos como method result. El parentesis no es un operator.
    @Test
    fun test016_testParenthesesInsideParentheses() {
        val code = "let sum: number = ((3 + 5) * (2 + 4));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 37),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(27, 27),
                            Call(
                                Range(19, 35),
                                "*",
                                listOf(
                                    MethodResult(
                                        Range(22, 22),
                                        Call(
                                            (Range(20, 24)),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(20, 20), "3", "number"),
                                                LiteralArgument(Range(24, 24), "5", "number"),
                                            ),
                                        ),
                                    ),
                                    MethodResult(
                                        Range(32, 32),
                                        Call(
                                            Range(30, 34),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(30, 30), "2", "number"),
                                                LiteralArgument(Range(34, 34), "4", "number"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    // falta resolver el tema de las comas. No son operadores, son SEPARADORES.
    // Quiza, en el getFinalArgumentsOfMethodResult fijarse en vez del tamaño, si hay operadores.
    @Test
    fun test017_testMethodDeclarationOfAMethodCall() {
        val code = "let test: number = sum(3, 5);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 28),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "number",
                        MethodResult(
                            Range(19, 21),
                            Call(
                                Range(23, 26),
                                "sum",
                                listOf(
                                    LiteralArgument(Range(23, 23), "3", "number"),
                                    LiteralArgument(Range(26, 26), "5", "number"),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test018_testMethodDeclarationWithOperationInside() {
        val code = "let test: number = sum(3, 5 + 2);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 32),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "number",
                        MethodResult(
                            Range(19, 21),
                            Call(
                                Range(23, 30),
                                "sum",
                                listOf(
                                    LiteralArgument(Range(23, 23), "3", "number"),
                                    MethodResult(
                                        Range(28, 28),
                                        Call(
                                            Range(26, 30),
                                            "+",
                                            listOf(
                                                LiteralArgument(Range(26, 26), "5", "number"),
                                                LiteralArgument(Range(30, 30), "2", "number"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test019_testMethodInsideMethod() {
        val code = "let test: number = sum(3, sum(5, 2));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 36),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "number",
                        MethodResult(
                            Range(19, 21),
                            Call(
                                Range(23, 34),
                                "sum",
                                listOf(
                                    LiteralArgument(Range(23, 23), "3", "number"),
                                    MethodResult(
                                        Range(26, 28),
                                        Call(
                                            Range(30, 33),
                                            "sum",
                                            listOf(
                                                LiteralArgument(Range(30, 30), "5", "number"),
                                                LiteralArgument(Range(33, 33), "2", "number"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    // lo que pasa en sum(3, sum(5, 2)) es que ya de una te toma tres metodos: 3, sum(5 y 2)
    @Test
    fun test020_testSearchForClosingBracketsMethod() {
        val code = "let test: number = sum(3, sum(5, 2));"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 6)
        val expected = Result.success(15)
        assertEquals(expected, gotChar)
    }

    @Test
    fun test021_testSearchForClosingBracketsMethodPt2() {
        val code = "let test: number = sum(3, sum(5, 2));"

        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 10)
        val expected = Result.success(14)
        assertEquals(expected, gotChar)
    }

    @Test
    fun test022_testComplexEcuationWithParenthesesButParenthesesAreNotIncludedNumberheAST() {
        val code = "let test: number = 3 + (5 * 2) + 4;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 34),
                listOf(
                    VariableDeclaration(
                        Range(4, 7),
                        "test",
                        "number",
                        MethodResult(
                            Range(21, 21),
                            Call(
                                Range(19, 33),
                                "+",
                                listOf(
                                    LiteralArgument(Range(19, 19), "3", "number"),
                                    MethodResult(
                                        Range(31, 31),
                                        Call(
                                            Range(23, 33),
                                            "+",
                                            listOf(
                                                MethodResult(
                                                    Range(26, 26),
                                                    Call(
                                                        Range(24, 28),
                                                        "*",
                                                        listOf(
                                                            LiteralArgument(Range(24, 24), "5", "number"),
                                                            LiteralArgument(Range(28, 28), "2", "number"),
                                                        ),
                                                    ),
                                                ),
                                                LiteralArgument(Range(33, 33), "4", "number"),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(expected, ast)
    }

    @Test
    fun test023_expresionStatementTest() {
        val code = "let a: number;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 13),
                listOf(DeclarationStatement(Range(4, 4), "a", "number")),
            ))
        assertEquals(ast, expected)
    }

    @Test
    fun test024_variableDeclarationnumberwoDifferentParts() {
        val code = "let a: number; a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 21),
                listOf(
                    DeclarationStatement(Range(4, 4), "a", "number"),
                    AssignmentStatement(Range(15, 15), "a", LiteralArgument(Range(19, 20), "54", "number")),
                ),
            ))

        assertEquals(ast, expected)
    }

    @Test
    fun test025_assignementStatement() {
        val code = "a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 6),
                listOf(AssignmentStatement(Range(0, 0), "a", LiteralArgument(Range(4, 5), "54", "number"))),
            ))

        assertEquals(ast, expected)
    }

    @Test
    fun test026_assignementStatementArgumentIsLiteralAndCorrect() {
        val code = "a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            it as Scope
            val first = it.body.first()
            if (first is AssignmentStatement) {
                assertEquals(first.value, LiteralArgument(Range(4, 5), "54", "number"))
            }
        }
    }

    @Test
    fun test027_testSumOfTwoVariableArguments() {
        val code = "let sum: number = a + b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 23),
                listOf(
                    VariableDeclaration(
                        Range(4, 6),
                        "sum",
                        "number",
                        MethodResult(
                            Range(20, 20),
                            Call(
                                (Range(18, 22)),
                                "+",
                                listOf(
                                    VariableArgument(Range(18, 18), "a"),
                                    VariableArgument(Range(22, 22), "b"),
                                ),
                            ),
                        ),
                    ),
                ),
            ))
        assertEquals(expected, ast)
    }

    @Test
    fun test028_testSumOfTwoVariableArguments_withFirstVariableArgBeingAlsoTheIdentifier() {
        val code = "let a: number = a + b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 21),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "number",
                        MethodResult(
                            Range(18, 18),
                            Call(
                                (Range(16, 20)),
                                "+",
                                listOf(
                                    VariableArgument(Range(16, 16), "a"),
                                    VariableArgument(Range(20, 20), "b"),
                                ),
                            ),
                        ),
                    ),
                ),
            ))
        assertEquals(expected, ast)
    }

    @Test
    fun test029_testIsolatedMethodCall() {
        val code = "println(34);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 11),
                listOf(
                    MethodResult(
                        Range(0, 6),
                        Call(Range(8, 9), "println", listOf(LiteralArgument(Range(8, 9), "34", "number"))),
                    ),
                ),
            ))
        assertEquals(expected, ast)
    }

    @Test
    fun test030_testIsolatedMethodCallWithInsideMethod() {
        val code = "println(34 + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
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
                                            LiteralArgument(Range(8, 9), "34", "number"),
                                            LiteralArgument(Range(13, 13), "4", "number"),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))
        assertEquals(expected, ast)
    }

    @Test
    fun test031_testEmptyMethodCall() {
        val code = "object();"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        val expected =
            Result.success(Scope("program", Range(0, 8), listOf(MethodResult(Range(0, 7), Call(Range(0, 7), "object", listOf())))))
        assertEquals(expected, ast)
    }

    @Test
    fun test032_testEmptyMethodCallWithVariableDeclaration() {
        val code = "let a: String = object();"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 24),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "String",
                        MethodResult(Range(16, 23), Call(Range(16, 23), "object", listOf())),
                    ),
                ),
            ))
        assertEquals(ast, expected)
    }

    @Test
    fun test032_testVariableDeclarationWithAnObjectThatANumberStringNorNumberNorBool() {
        val code = "let a: object = object();"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(Scope(
                "program",
                Range(0, 24),
                listOf(
                    VariableDeclaration(
                        Range(4, 4),
                        "a",
                        "object",
                        MethodResult(Range(16, 23), Call(Range(16, 23), "object", listOf())),
                    ),
                ),
            ))
        assertEquals(ast, expected)
    }

    @Test
    fun test033_testprintlnWithSumOfStringAndNumber() {
        val code = "println(\"Hello\" + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        val expected =
            Result.success(Scope(
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
                                            LiteralArgument(Range(8, 14), "Hello", "string"),
                                            LiteralArgument(Range(18, 18), "4", "number"),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(ast, expected)
    }

    @Test
    fun test034_testWithStringsOFTripleQuote() {
        val code = """println("Hello" + 4);"""
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)

        val expected =
            Result.success(Scope(
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
                                            LiteralArgument(Range(8, 14), "Hello", "string"),
                                            LiteralArgument(Range(18, 18), "4", "number"),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ))

        assertEquals(ast, expected)
    }
}
