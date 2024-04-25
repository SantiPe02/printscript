public class ParserStatementTest {
    @Test
    fun test023_expressionStatementTest() {
        val code = "let a: number;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
                Result.success(
                        Scope(
                                "program",
                                Range(0, 13),
                                listOf(DeclarationStatement(Range(4, 4), "a", "number")),
                                ),
                        )
        assertEquals(ast, expected)
    }

    @Test
    fun test024_variableDeclarationNumberwoDifferentParts() {
        val code = "let a: number; a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
                Result.success(
                        Scope(
                                "program",
                                Range(0, 21),
                                listOf(
                                        DeclarationStatement(Range(4, 4), "a", "number"),
                                        AssignmentStatement(Range(15, 15), "a", LiteralArgument(Range(19, 20), "54", "number")),
                                        ),
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
                Result.success(
                        Scope(
                                "program",
                                Range(0, 6),
                                listOf(AssignmentStatement(Range(0, 0), "a", LiteralArgument(Range(4, 5), "54", "number"))),
                                ),
                        )

        assertEquals(ast, expected)
    }

    @Test
    fun test026_assignementStatementArgumentIsLiteralAndCorrect() {
        val code = "a = 54;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val first = it.body.first()
            if (first is AssignmentStatement) {
                assertEquals(first.value, LiteralArgument(Range(4, 5), "54", "number"))
            }
        }
    }

}
