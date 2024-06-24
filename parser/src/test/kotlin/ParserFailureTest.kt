import factory.LexerFactoryImpl
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

public class ParserFailureTest {
    private val lexer = LexerFactoryImpl("1.1").create()

    @Test
    fun `test001 failure on simple string declaration`() {
        val code = "var a: String = Hello\";"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test002 failure on no closing coma`() {
        val code = "var a: String = \"Hello\""
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test003 failure on no type declaration`() {
        val code = "var a = \"Hello\";"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test004 failure on incorrect method declaration`() {
        val code = "let a:int = object(a, b,);"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test005 failure on no closing parenthesis`() {
        val code = "let a: int = object(a, b, c; b = messi);"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test006 a LITERAL followed imediately by a IDENTIFIER in declaration throws error`() {
        val code = "let a : string = \"FOO\" println(readEnv(a));"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        println(ast)
        assert(ast.isFailure)
    }

    @Test
    fun `test007 two consecutive OPERATORS in declaration throws error`() {
        val code = "let a: int = 1 + + 2;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test008 two consecutive IDENTIFIERS in declaration throws error`() {
        val code = "let a: int = b c;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test009 two consecutive LITERALS in declaration throws error`() {
        val code = "let a: int = 1 2;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }

    @Test
    fun `test008 two consecutive IDENTIFIERS in throws error`() {
        val code = "let a b: int = c;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        assert(ast.isFailure)
    }
}
