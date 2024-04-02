package formatter

import formater.RegexFormatter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RegexFormatterOptionalRulesTest {
    @Test
    fun test001_spaceBeforeColon_ifSpaceBeforeColonThenSpaceBeforeColon() {
        val text = " :"
        Assertions.assertEquals(text, RegexFormatter(listOf(RegexFormatter.Rule(":", " :"))).formatString(text))
    }

    @Test
    fun test002_spaceBeforeColon_ifColonThenSpaceBeforeColon() {
        val text = ":"
        Assertions.assertEquals(" :", RegexFormatter(listOf(RegexFormatter.Rule(":", " :"))).formatString(text))
    }

    @Test
    fun test003_spaceAfterColon_ifSpaceAfterColonThenSpaceAfterColon() {
        val text = ": "
        Assertions.assertEquals(text, RegexFormatter(listOf(RegexFormatter.Rule(":", ": "))).formatString(text))
    }

    @Test
    fun test004_spaceAfterColon_ifColonThenSpaceAfterColon() {
        val text = ":"
        Assertions.assertEquals(": ", RegexFormatter(listOf(RegexFormatter.Rule(":", ": "))).formatString(text))
    }

    @Test
    fun test005_spaceBeforeAndAfterColon_ifColonThenSpaceBeforeAndAfter() {
        val text = ":"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule(":", " : ")))
        Assertions.assertEquals(" : ", formatter.formatString(text))
    }

    @Test
    fun test006_noSpaceBeforeColon_ifColonThenColon() {
        val text = ":"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*:", ":")))
        Assertions.assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test007_noSpaceBeforeColon_ifSpaceColonThenColon() {
        val text = " :"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*:", ":")))
        Assertions.assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test008_noSpaceAfterColon_ifColonThenColon() {
        val text = ":"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule(":\\s*", ":")))
        Assertions.assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test009_noSpaceAfterColon_ifColonSpaceThenColon() {
        val text = ": "
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule(":\\s*", ":")))
        Assertions.assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test010_noSpaceBeforeAndAfterColon_ifSpaceColonSpaceThenColon() {
        val text = " : "
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*:\\s*", ":")))
        Assertions.assertEquals(":", formatter.formatString(text))
    }

    @Test
    fun test011_noSpaceAssignation_ifAssignationThenAssignation() {
        val text = "="
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", "=")))
        Assertions.assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test012_noSpaceAssignation_ifSpaceAssignationThenAssignation() {
        val text = " ="
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", "=")))
        Assertions.assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test012_noSpaceAssignation_ifAssignationSpaceThenAssignation() {
        val text = "= "
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", "=")))
        Assertions.assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test013_noSpaceAssignation_ifSpaceAssignationSpaceThenAssignation() {
        val text = " = "
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", "=")))
        Assertions.assertEquals("=", formatter.formatString(text))
    }

    @Test
    fun test014_spacedAssignation_ifSpaceAssignationSpaceThenSpaceAssignationSpace() {
        val text = " = "
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", " = ")))
        Assertions.assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test015_spacedAssignation_ifSpaceAssignationThenSpaceAssignationSpace() {
        val text = " ="
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", " = ")))
        Assertions.assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test016_spacedAssignation_ifAssignationSpaceThenSpaceAssignationSpace() {
        val text = "= "
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", " = ")))
        Assertions.assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test017_spacedAssignation_ifAssignationThenSpaceAssignationSpace() {
        val text = "="
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\\s*=\\s*", " = ")))
        Assertions.assertEquals(" = ", formatter.formatString(text))
    }

    @Test
    fun test018_changeLinePrintln_ifPrintlnThenPrintln() {
        val text = "println(\"text\")"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\n{4,}println", "\n\n\nprintln")))
        Assertions.assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test019_changeLinePrintln_ifNPrintlnThenNPrintln() {
        val text = "\nprintln(\"text\")"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\n{4,}println", "\n\n\nprintln")))
        Assertions.assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test020_changeLinePrintln_if2NPrintlnThen2NPrintln() {
        val text = "\n\nprintln(\"text\")"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\n{4,}println", "\n\n\nprintln")))
        Assertions.assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test021_changeLinePrintln_if3NPrintlnThen3NPrintln() {
        val text = "\n\n\nprintln(\"text\")"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\n{4,}println", "\n\n\nprintln")))
        Assertions.assertEquals(text, formatter.formatString(text))
    }

    @Test
    fun test021_changeLinePrintln_if4NPrintlnThen3NPrintln() {
        val text = "\n\n\n\nprintln(\"text\")"
        val formatter = RegexFormatter(listOf(RegexFormatter.Rule("\n{4,}println", "\n\n\nprintln")))
        Assertions.assertEquals("\n\n\nprintln(\"text\")", formatter.formatString(text))
    }

    @Test
    fun test021_multiRules() {
        val text = "let a :number=12;let b:number = 4;a= a/b;\n\n\n\nprintln(\"Result \" + a);"
        val formatter = RegexFormatter(
            listOf(
                RegexFormatter.Rule("\\s*=\\s*", " = "),
                RegexFormatter.Rule(":", " : "),
                RegexFormatter.Rule("\n{4,}println", "\n\n\nprintln")
            )
        )
        Assertions.assertEquals(
            "let a : number = 12;\nlet b : number = 4;\na = a / b;\n\n\nprintln(\"Result \" + a);",
            formatter.formatString(text)
        )
    }

}