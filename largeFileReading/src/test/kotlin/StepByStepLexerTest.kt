import lexer.LexerImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StepByStepLexerTest {
    @Test
    fun test001_ifSplitInSpaceAllGood() {
        var reader = PartialStringReading(LexerImpl())
        val text = "let a : number = 10;"

        val result = reader.tokenizeString(text.substring(0, 15))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(15)).second
        assertEquals(LexerImpl().tokenize(text), tokens)
    }

    @Test
    fun test002_SplittingNumber() {
        var reader = PartialStringReading(LexerImpl())
        val text = "let a : number = 10;"

        val split = 10

        val result = reader.tokenizeString(text.substring(0, split))
        reader = result.first
        var tokens = result.second

        tokens += reader.tokenizeString(text.substring(split)).second
        assertEquals(LexerImpl().tokenize(text), tokens)
    }

    /*
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
