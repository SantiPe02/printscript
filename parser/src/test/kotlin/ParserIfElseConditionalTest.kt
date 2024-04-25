import ast.*
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser

class ParserIfElseConditionalTest {

    @Test
    fun `test001 test simple boolean if and else with empty scopes`(){
        val code = "if(a){} else{}"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 13),
            listOf(
                IfAndElseStatement(
                    Range(0, 13),
                    listOf(BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))),
                    Scope("program", Range(5, 6), emptyList()),
                    Scope("program", Range(12, 13), emptyList())
                )
            )
        )

        Assertions.assertEquals(Result.success(expected), ast)
    }

}