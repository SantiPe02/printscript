import factory.LexerFactoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stringReader.PartialStringReadingLexer

class StepByStepLexerTest {
    private val lexer = LexerFactoryImpl("1.1").create()

    @Test
    fun test001_ifSplitInSpaceAllGood() {
        var reader = PartialStringReadingLexer(lexer)
        val text = "let a : number = 10;"

        val result = reader.tokenizeString(text.substring(0, 15), false)
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(15), false).second
        assertEquals(lexer.tokenize(text), tokens)
    }

    @Test
    fun test002_SplittingNumber() {
        var reader = PartialStringReadingLexer(lexer)
        val text = "let a : number = 10;"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split), false)
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split), false).second
        assertEquals(lexer.tokenize(text), tokens)
    }

    @Test
    fun `test004 two variable declarations`() {
        var reader = PartialStringReadingLexer(lexer)
        val text = "let a : number = 10; let b : number = 20;"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split), false)
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split), false).second

        assertEquals(lexer.tokenize(text), tokens)
    }

    @Test
    fun `test005 test if statement`() {
        var reader = PartialStringReadingLexer(lexer)
        val text = "if (a) { println(\"a is 10\"); }"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split), false)
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split), false).second

        assertEquals(lexer.tokenize(text), tokens)
    }

    @Test
    fun `test006 should lex even if there ain't no semicolon or closing bracket`() {
        var reader = PartialStringReadingLexer(lexer)
        val text = "println(\"a is true\")"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split), false)
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split), true).second
        assertEquals(lexer.tokenize(text), tokens)
    }
}
