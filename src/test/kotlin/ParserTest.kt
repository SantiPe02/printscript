import ast.*
import lexer.Lexer
import lexer.LexerImpl
import parser.MyParser
import parser.Parser
import kotlin.test.DefaultAsserter.assertEquals


class ParserTest {
    fun testSimpleLiteralVariableDeclaration(){
        val code = "let a: Int = 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 0),
            listOf( VariableDeclaration( Range(0, 0), "a", "Int", LiteralArgument(Range(0, 0), "5", "Int"))))
        assertEquals(expected, ast)
    }
}