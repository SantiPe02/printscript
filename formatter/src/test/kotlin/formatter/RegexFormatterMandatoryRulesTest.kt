package formatter

import formater.RegexFormatter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RegexFormatterMandatoryRulesTest {
    @Test
    fun test001_spacesBetweenTokens_NiceSpacingToNiceSpacing() {
        val text = "let a : String = \"this is a string\";"
        Assertions.assertEquals(text, RegexFormatter().formatString(text))
    }

    @Test
    fun test002_spacesBetweenTokens_FromMultiSpacesToMaxOneSpaceFormat() {
        val text = "let  a    : String     =    \"this is a string\";"
        val expected = "let a : String = \"this is a string\";"
        Assertions.assertEquals(expected, RegexFormatter().formatString(text))
    }

    @Test
    fun test003_spacesBetweenToken_NoSpacesWhereNotNeededThenDontAddSpaces() {
        val text = "let a : String = \"this is a string\";"
        Assertions.assertEquals(text, RegexFormatter().formatString(text))
    }

    @Test
    fun test004_newLineAfterSemicolon_IfCorrectFormatThenCorrectFormat() {
        val text = "let a : String = \"this is a string\";\na = \"This is also a string\";"
        Assertions.assertEquals(text, RegexFormatter().formatString(text))
    }

    @Test
    fun test005_newLineAfterSemicolon_IfNoNewLineAddNewLineFormat() {
        val text = "let a : String = \"this is a string\";a = \"This is also a string\";"
        val expected = "let a : String = \"this is a string\";\na = \"This is also a string\";"
        Assertions.assertEquals(expected, RegexFormatter().formatString(text))
    }

    // TODO: newLineAfeterSemicolon_IfNoNewLineAndSomeSpacesAssNewLineFormat
    // ("let a:String=\"this is a string\";    a=\"This is also a string\";")

    @Test
    fun test008_SpaceBeforeAndAfetOperator_NoSpacesAndSomeValuesThenAddSpacesBetweenOperatorAndValues() {
        for (operator in listOf("+", "-", "*", "/")) {
            val text = "let c : Int = a${operator}b${operator}d;"
            Assertions.assertEquals("let c : Int = a $operator b $operator d;", RegexFormatter().formatString(text))
        }
    }

    @Test
    fun test009_formattingPatternsInsideText_ThenIgnoreFormatting() {
        val text = "let a : String = \"this is a ; string\";\na = \"This is+also a     string\";"
        Assertions.assertEquals(text, RegexFormatter().formatString(text))
    }
}
