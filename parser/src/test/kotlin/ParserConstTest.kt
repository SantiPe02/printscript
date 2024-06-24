import ast.ConstantDeclaration
import ast.LiteralArgument
import ast.Range
import ast.Scope
import factory.LexerFactoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser
import parser.ParserCommons

class ParserConstTest {
    val commons = ParserCommons()
    private val lexer = LexerFactoryImpl("1.1").create()

    @Test
    fun `test001 const simple declaration`() {
        val code = "const a: string = \"Hello\";"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected =
            Result.success(
                Scope(
                    "program",
                    Range(0, 25),
                    listOf(
                        ConstantDeclaration(
                            Range(6, 6),
                            "a",
                            "string",
                            LiteralArgument(Range(18, 24), "Hello", "string"),
                        ),
                    ),
                ),
            )

        assertEquals(expected, ast)
    }

    @Test
    fun `test002 const declaration failure when const should always be declared fully`() {
        val code = "const a: string;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test003 const declaration failure when const has equals but no argument`() {
        val code = "const a: string = ;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }
}
