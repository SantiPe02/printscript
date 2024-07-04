package formatter

import factory.LexerFactoryImpl
import formater.FormatterConfiguration
import formater.astFormatter.ASTFormatter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.MyParser

class ASTFormatterTest {
    private val lexer = LexerFactoryImpl("1.1").create()
    private val parser = MyParser()

    @Test
    fun testBasicASTFormatter() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "let a   :  number= 5;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a: number = 5;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testASTFormatterWithLineSkip() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration(lineJumpBeforePrintln = 2))
        val sourceCode = "   let a  \n  :  number =5  ;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a: number = 5;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun methodCall() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "println   (    \"Hello\" );"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "\nprintln(\"Hello\");"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun methodCallWithMoreArguments() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "println   (    \"Hello\", \n 5, 6 )   ;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "\nprintln(\"Hello\", 5, 6);"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testIfStatement() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "if (a) {println(\"Hello\");}"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "if (a) {\n    \n    println(\"Hello\");\n}"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testCodeWithSeveralLinesAndDifferentDeclarationsAndCalls() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "\nlet a   \n :number =5;    let \n  b    : \nstring= \"Hello\";println  (  a);    println(b);"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a: number = 5;\nlet b: string = \"Hello\";\n\nprintln(a);\n\nprintln(b);"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testIfElseStatement() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "if (a) {println(\"Hello\");} else {println(7);}"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "if (a) {\n    \n    println(\"Hello\");\n} else {\n    \n    println(7);\n}"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testIfElseStatementWithSeveralLinesEach() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode =
            "if (a) {println(\"Hello\");let  b:  number=2; println(b);} else {println(7); let c: string = \"bye\"; println(c);}"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode =
            "if (a) {\n    \n    println(\"Hello\");\n    let b: number = 2;\n    \n    println(b);\n} else {\n    \n    println(7);\n    let c: string = \"bye\";\n    \n    println(c);\n}"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testIfStatementWithVariableDeclaration() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "if (a) {let a: string = \"hello\"; println(a);}"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "if (a) {\n    let a: string = \"hello\";\n    \n    println(a);\n}"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testWithDifferentConfigurations() {
        val formatterConfig =
            FormatterConfiguration(
                spaceBeforeColon = false,
                spaceAfterColon = false,
                spaceBeforeAndAfterEqual = false,
                lineJumpBeforePrintln = 2,
                indentation = 2,
            )
        val astFormatter = ASTFormatter(lexer, parser, formatterConfig)
        val sourceCode = "let a   :  number= 5;  println   (    \"Hello\", \n 5, 6 )   ;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a:number=5;\n\n\nprintln(\"Hello\", 5, 6);"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testWithOperators() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "let    \na    :  number =    5+3;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a: number = 5 + 3;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun constDeclarationTest() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "const \n   a  :    number    = 5;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "const a: number = 5;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testWithDifferentConfigurations2() {
        val formatterConfig =
            FormatterConfiguration(
                spaceBeforeColon = true,
                spaceAfterColon = true,
                spaceBeforeAndAfterEqual = true,
                lineJumpBeforePrintln = 2,
                indentation = 2,
            )
        val astFormatter = ASTFormatter(lexer, parser, formatterConfig)
        val sourceCode = "let a   :  number= 5;  println   (    \"Hello\", \n 5, 6 )   ;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a : number = 5;\n\n\nprintln(\"Hello\", 5, 6);"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testWithDifferentConfigurations3() {
        val formatterConfig =
            FormatterConfiguration(
                spaceBeforeColon = true,
                spaceAfterColon = false,
                spaceBeforeAndAfterEqual = true,
                lineJumpBeforePrintln = 2,
                indentation = 2,
            )
        val astFormatter = ASTFormatter(lexer, parser, formatterConfig)
        val sourceCode = "let a   :  number= 5;  println   (    \"Hello\", \n 5, 6 )   ;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a :number = 5;\n\n\nprintln(\"Hello\", 5, 6);"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testWithDifferentConfigurations4() {
        val formatterConfig =
            FormatterConfiguration(
                spaceBeforeColon = false,
                spaceAfterColon = true,
                spaceBeforeAndAfterEqual = true,
                lineJumpBeforePrintln = 2,
                indentation = 2,
            )
        val astFormatter = ASTFormatter(lexer, parser, formatterConfig)
        val sourceCode = "let a   :  number= 5;  println   (    \"Hello\", \n 5, 6 )   ;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a: number = 5;\n\n\nprintln(\"Hello\", 5, 6);"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun declarationStatementWithDefaultConfigurations() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "let a   :  number;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a: number;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun declarationStatementWithDifferentConfigurations() {
        val formatterConfig =
            FormatterConfiguration(
                spaceBeforeColon = true,
                spaceAfterColon = true,
                spaceBeforeAndAfterEqual = true,
                lineJumpBeforePrintln = 2,
                indentation = 2,
            )
        val astFormatter = ASTFormatter(lexer, parser, formatterConfig)
        val sourceCode = "let a   :  number;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "let a : number;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun assingmentStatementWithDefaultConfiguration() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "a    = 5;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "a = 5;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun assingmentStatementWithDifferentConfiguration() {
        val formatterConfig =
            FormatterConfiguration(
                spaceBeforeColon = true,
                spaceAfterColon = true,
                spaceBeforeAndAfterEqual = false,
                lineJumpBeforePrintln = 2,
                indentation = 2,
            )
        val astFormatter = ASTFormatter(lexer, parser, formatterConfig)
        val sourceCode = "a    =5;"
        val formattedCode = astFormatter.formatString(sourceCode)
        val expectedCode = "a=5;"
        Assertions.assertEquals(expectedCode, formattedCode)
    }
}
