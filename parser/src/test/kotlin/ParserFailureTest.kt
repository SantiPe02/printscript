import lexer.LexerImpl
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

public class ParserFailureTest {
    @Test
    fun `test001 failure on simple string declaration`() {
        val code = "var a: String = Hello\";"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test002 failure on no closing coma`() {
        val code = "var a: String = \"Hello\""
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test003 failure on no type declaration`() {
        val code = "var a = \"Hello\";"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test004 failure on incorrect method declaration`() {
        val code = "let a = object(a, b,);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }
}
