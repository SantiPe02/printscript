import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import stringReader.PartialStringReadingLexer

class StepByStepLexerTest {
    @Test
    fun test001_ifSplitInSpaceAllGood() {
        var reader = PartialStringReadingLexer(LexerImpl())
        val text = "let a : number = 10;"

        val result = reader.tokenizeString(text.substring(0, 15))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(15)).second
        assertEquals(LexerImpl().tokenize(text), tokens)
    }

    @Test
    fun test002_SplittingNumber() {
        var reader = PartialStringReadingLexer(LexerImpl())
        val text = "let a : number = 10;"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split)).second
        assertEquals(LexerImpl().tokenize(text), tokens)
    }

    @Test
    fun `test004 two variable declarations`() {
        var reader = PartialStringReadingLexer(LexerImpl())
        val text = "let a : number = 10; let b : number = 20;"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split)).second

        assertEquals(LexerImpl().tokenize(text), tokens)
    }

    @Test
    fun `test005 test if statement`() {
        var reader = PartialStringReadingLexer(LexerImpl())
        val text = "if (a) { println(\"a is 10\"); }"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split)).second

        assertEquals(LexerImpl().tokenize(text), tokens)
    }

    /*@Test
    fun `test006 should lex even if there ain't no semicolon or closing bracket`(){
        var reader = PartialStringReadingLexer(LexerImpl())
        val text = "println(\"a is true\")"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split)).second
        assertEquals(LexerImpl().tokenize(text), tokens)
    }
    @Test
    fun `test007 should lex the code that continues after last bracket or semicolon`(){
        var reader = PartialStringReadingLexer(LexerImpl())
        val text = "println(\"a is true\"); println(\"a is tr"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split)).second
        assertEquals(LexerImpl().tokenize(text), tokens)

    }

    @Test
    fun test003_colonInsideString() {
        var reader = PartialStringReading(LexerImpl())
        val text = "let a : number = \"here goes the ; thanks\";"

        val split = 36

        val result = reader.tokenizeString(text.substring(0, split))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split)).second
        assertEquals(LexerImpl().tokenize(text), tokens)
    }*/
}
