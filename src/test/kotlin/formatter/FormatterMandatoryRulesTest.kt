package formatter

import formater.Formatter
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterMandatoryRulesTest {

    @Test
    fun test001_spacesBetweenTokens_NiceSpacingToNiceSpacing() {
        val text = "let a : String = \"this is a string\";"
        assertEquals(text, Formatter().formatString(text))
    }

    @Test
    fun test002_spacesBetweenTokens_FromMultiSpacesToMaxOneSpaceFormat() {
        val text = "let  a    : String     =    \"this is a string\";"
        val expected = "let a : String = \"this is a string\";"
        assertEquals(expected, Formatter().formatString(text))
    }

    @Test
    fun test003_spacesBetweenToken_NoSpacesWhereNotNeededThenDontAddSpaces() {
        val text = "let a:String = \"this is a string\";"
        assertEquals(text, Formatter().formatString(text))
    }

    @Test
    fun test004_newLineAfterSemicolon_IfCorrectFormatThenCorrectFormat() {
        val text = "let a:String = \"this is a string\";\na = \"This is also a string\";"
        assertEquals(text, Formatter().formatString(text))
    }

    @Test
    fun test005_newLineAfterSemicolon_IfNoNewLineAddNewLineFormat() {
        val text = "let a:String = \"this is a string\";a = \"This is also a string\";"
        val expected = "let a:String = \"this is a string\";\na = \"This is also a string\";"
        assertEquals(expected, Formatter().formatString(text))
    }

    //TODO: newLineAfeterSemicolon_IfNoNewLineAndSomeSpacesAssNewLineFormat
    // ("let a:String=\"this is a string\";    a=\"This is also a string\";")


    @Test
    fun test006_SpaceBeforeAndAfterOperator_NiceFormatWithPlusThenNiceFormat() {
        for (operator in listOf("+", "-", "*", "/")){
            val text = " $operator "
            assertEquals(text, Formatter().formatString(text))
        }
    }

    @Test
    fun test007_SpaceBeforeAndAfterOperator_NoSpacesThenAddSpaces() {
        for (operator in listOf("+", "-", "*", "/")) {
            val text = operator
            assertEquals(" $operator ", Formatter().formatString(text))
        }
    }

    @Test
    fun test008_SpaceBeforeAndAfetOperator_NoSpacesAndSomeValuesThenAddSpacesBetweenOperatorAndValues() {
        for (operator in listOf("+", "-", "*", "/")){
            val text = "a${operator}b"
            assertEquals("a $operator b", Formatter().formatString(text))
        }
    }

    //TODO: should test for operators and ; inside a String literal.
}