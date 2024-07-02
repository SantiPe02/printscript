import factory.LexerFactoryImpl
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class ParserDeclaratorTest {
    private val lexer = LexerFactoryImpl("1.1").create()

    @Test
    fun `test001 parseByTokenType cannot handle SPECIAL CHARACTER`() {
        val code = ";"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test002 parseByTokenType cannot handle LITERAL`() {
        val code = "3"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test003 parseByTokenType cannot handle OPERATOR`() {
        val code = "="
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }
}
