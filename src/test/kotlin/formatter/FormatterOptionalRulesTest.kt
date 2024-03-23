package formatter

import formater.Formatter
import org.junit.jupiter.api.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals

class FormatterOptionalRulesTest {
    @Test
    fun test001_spaceBeforeColon_ifSpaceBeforeColonThenSpaceBeforeColon() {
        val text = " :"
        assertEquals(text, Formatter(listOf(Formatter.Rule(":", " :"))).formatString(text))
    }

    @Test
    fun test002_spaceBeforeColon_ifColonThenSpaceBeforeColon() {
        val text = ":"
        assertEquals(" :", Formatter(listOf(Formatter.Rule(":", " :"))).formatString(text))
    }

    @Test
    fun test003_spaceAfterColon_ifSpaceAfterColonThenSpaceAfterColon() {
        val text = ": "
        assertEquals(text, Formatter(listOf(Formatter.Rule(":", ": "))).formatString(text))
    }

    @Test
    fun test004_spaceAfterColon_ifColonThenSpaceAfterColon() {
        val text = ":"
        assertEquals(": ", Formatter(listOf(Formatter.Rule(":", ": "))).formatString(text))
    }

    @Test
    fun test005_spaceBeforeAndAfterColon_ifColonThenSpaceBeforeAndAfter() {
        val text = ":"
        val formatter = Formatter(listOf(Formatter.Rule(":", " : ")))
        assertEquals(" : ", formatter.formatString(text))
    }

    @Test
    fun test006_noSpaceBeforeColon_ifColonThenColon() {
        val text = ":"
        val formatter = Formatter(listOf(Formatter.Rule("\\s*:", ":")))
        assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test007_noSpaceBeforeColon_ifSpaceColonThenColon() {
        val text = " :"
        val formatter = Formatter(listOf(Formatter.Rule("\\s*:", ":")))
        assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test008_noSpaceAfterColon_ifColonThenColon() {
        val text = ":"
        val formatter = Formatter(listOf(Formatter.Rule(":\\s*", ":")))
        assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test009_noSpaceAfterColon_ifColonSpaceThenColon() {
        val text = ": "
        val formatter = Formatter(listOf(Formatter.Rule(":\\s*", ":")))
        assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test010_noSpaceBeforeAndAfterColon_ifSpaceColonSpaceThenColon() {
        val text = " : "
        val formatter = Formatter(listOf(Formatter.Rule("\\s*:\\s*", ":")))
        assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test011_noSpaceAssignation_ifAssignationThenAssignation() {
        val text = "="
        val formatter = Formatter(listOf(Formatter.Rule("\\s*=\\s*", "=")))
        assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test012_noSpaceAssignation_ifSpaceAssignationThenAssignation() {
        val text = " ="
        val formatter = Formatter(listOf(Formatter.Rule("\\s*=\\s*", "=")))
        assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test012_noSpaceAssignation_ifAssignationSpaceThenAssignation() {
        val text = "= "
        val formatter = Formatter(listOf(Formatter.Rule("\\s*=\\s*", "=")))
        assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test013_noSpaceAssignation_ifSpaceAssignationSpaceThenAssignation() {
        val text = " = "
        val formatter = Formatter(listOf(Formatter.Rule("\\s*=\\s*", "=")))
        assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test014_spacedAssignation_ifSpaceAssignationSpaceThenSpaceAssignationSpace() {
        val text = " = "
        val formatter = Formatter(listOf(Formatter.Rule("=", " = ")))
        assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test015_spacedAssignation_ifSpaceAssignationThenSpaceAssignationSpace() {
        val text = " ="
        val formatter = Formatter(listOf(Formatter.Rule("=", " = ")))
        assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test016_spacedAssignation_ifAssignationSpaceThenSpaceAssignationSpace() {
        val text = "= "
        val formatter = Formatter(listOf(Formatter.Rule("=", " = ")))
        assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test017_spacedAssignation_ifAssignationThenSpaceAssignationSpace() {
        val text = "="
        val formatter = Formatter(listOf(Formatter.Rule("=", " = ")))
        assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test018_changeLinePrintln_ifPrintlnThenPrintln() {
        val text = "println(\"text\")"
        val formatter = Formatter(listOf(Formatter.Rule("\n{4,}println", "\n\n\nprintln")))
        assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test019_changeLinePrintln_ifNPrintlnThenNPrintln() {
        val text = "\nprintln(\"text\")"
        val formatter = Formatter(listOf(Formatter.Rule("\n{4,}println", "\n\n\nprintln")))
        assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test020_changeLinePrintln_if2NPrintlnThen2NPrintln() {
        val text = "\n\nprintln(\"text\")"
        val formatter = Formatter(listOf(Formatter.Rule("\n{4,}println", "\n\n\nprintln")))
        assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test021_changeLinePrintln_if3NPrintlnThen3NPrintln() {
        val text = "\n\n\nprintln(\"text\")"
        val formatter = Formatter(listOf(Formatter.Rule("\n{4,}println", "\n\n\nprintln")))
        assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test021_changeLinePrintln_if4NPrintlnThen3NPrintln() {
        val text = "\n\n\n\nprintln(\"text\")"
        val formatter = Formatter(listOf(Formatter.Rule("\n{4,}println", "\n\n\nprintln")))
        assertEquals("\n\n\nprintln(\"text\")", formatter.formatString(text))
    }

    @Test
    fun test021_multiRules() {
        val text = "let a :number=12;let b:number = 4;a= a/b;\n\n\n\nprintln(\"Result \" + a);"
        val formatter = Formatter(listOf(
            Formatter.Rule("\n{4,}println", "\n\n\nprintln"),
            Formatter.Rule("\\s*=\\s*", " = "),
            Formatter.Rule(":", " : ")
        ))
        assertEquals(
            "let a : number = 12;\nlet b : number = 4;\na = a / b;\n\n\nprintln(\"Result \" + a);",
            formatter.formatString(text)
        )
    }



    //TODO: check that all this does not work inside of String literals

    @Ignore //TODO: this should be for file formatter
    @Test
    fun test_sameTextOfTest003ButSplitedIntoTextAndThenMergedResultingInTheSameOutput() {
        val text1 = "let a : String = \"this is a string\";\n"
        val text2 = "a = \"this is also a string\";"
        val formatter =  Formatter()
        assertEquals(text1+text2, formatter.formatString(text1)+formatter.formatString(text2))
    }
}