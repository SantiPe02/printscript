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

class ParserCommonsTest {

    @Test
    fun test013_testSearchForClosingCharacter() {
        val code = "((()))))"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens, "(", 0)
        val expected = Result.success(5)
        assertEquals(expected, gotChar)
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


}
