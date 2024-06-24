import factory.LexerFactoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import parser.ParserCommons

class ParserCommonsTest {
    val commons = ParserCommons()
    private val lexer = LexerFactoryImpl("1.1").create()

    @Test
    fun test001_testSearchForClosingCharacter() {
        val code = "((()))))"
        val tokens = lexer.tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 0)
        val expected = Result.success(5)
        assertEquals(expected, gotChar)
    }

    // lo que pasa en sum(3, sum(5, 2)) es que ya de una te toma tres metodos: 3, sum(5 y 2)
    @Test
    fun test002_testSearchForClosingBracketsMethod() {
        val code = "let test: number = sum(3, sum(5, 2));"
        val tokens = lexer.tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 6)
        val expected = Result.success(15)
        assertEquals(expected, gotChar)
    }

    @Test
    fun test003_testSearchForClosingBracketsMethodPt2() {
        val code = "let test: number = sum(3, sum(5, 2));"

        val tokens = lexer.tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 10)
        val expected = Result.success(14)
        assertEquals(expected, gotChar)
    }
}
