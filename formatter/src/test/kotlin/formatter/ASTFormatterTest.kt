package formatter

import ast.BooleanCondition
import ast.Call
import ast.ElseStatement
import ast.IfStatement
import ast.LiteralArgument
import ast.MethodResult
import ast.Range
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration
import formater.FormatterConfiguration
import formater.astFormatter.ASTFormatter
import lexer.LexerImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.MyParser

class ASTFormatterTest {
    private val lexer = LexerImpl()
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
        val ast =
            Scope(
                "program",
                Range(0, 25),
                listOf(
                    IfStatement(
                        Range(0, 25),
                        listOf(BooleanCondition(Range(4, 4), VariableArgument(Range(4, 4), "a"))),
                        listOf(
                            Scope(
                                "program",
                                Range(8, 24),
                                listOf(
                                    MethodResult(
                                        Range(8, 14),
                                        Call(Range(16, 22), "println", listOf(LiteralArgument(Range(16, 22), "Hello", "string"))),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        val formattedCode = astFormatter.processAST(ast)
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
        val ast =
            Scope(
                "program",
                Range(0, 45),
                listOf(
                    IfStatement(
                        Range(0, 45),
                        listOf(BooleanCondition(Range(4, 4), VariableArgument(Range(4, 4), "a"))),
                        listOf(
                            Scope(
                                "program",
                                Range(8, 28),
                                listOf(
                                    MethodResult(
                                        Range(8, 14),
                                        Call(Range(16, 22), "println", listOf(LiteralArgument(Range(16, 22), "Hello", "string"))),
                                    ),
                                ),
                            ),
                        ),
                    ),
                    ElseStatement(
                        Range(29, 45),
                        listOf(
                            Scope(
                                "program",
                                Range(35, 45),
                                listOf(
                                    MethodResult(
                                        Range(35, 41),
                                        Call(Range(43, 44), "println", listOf(LiteralArgument(Range(43, 44), "7", "number"))),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        val formattedCode = astFormatter.processAST(ast)
        val expectedCode = "if (a) {\n    \n    println(\"Hello\");\n}\nelse {\n    \n    println(7);\n}"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testIfStatementWithVariableDeclaration() {
        val astFormatter = ASTFormatter(lexer, parser, FormatterConfiguration())
        val sourceCode = "if (a) {let a: string = \"hello\"; println(a);}"
        val ast =
            Scope(
                "program",
                Range(0, 47),
                listOf(
                    IfStatement(
                        Range(0, 47),
                        listOf(BooleanCondition(Range(4, 4), VariableArgument(Range(4, 4), "a"))),
                        listOf(
                            Scope(
                                "program",
                                Range(8, 46),
                                listOf(
                                    VariableDeclaration(
                                        Range(8, 30),
                                        "a",
                                        "string",
                                        LiteralArgument(Range(24, 30), "hello", "string"),
                                    ),
                                    MethodResult(
                                        Range(32, 42),
                                        Call(Range(40, 41), "println", listOf(VariableArgument(Range(40, 41), "a"))),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        val formattedCode = astFormatter.processAST(ast)
        val expectedCode = "if (a) {\n    let a: string = \"hello\";\n    \n    println(a);\n}"
        Assertions.assertEquals(expectedCode, formattedCode)
    }

    @Test
    fun testWithDifferentConfigurations() {
        val formatterConfig =
            FormatterConfiguration(
                spaceBeforeColon = false,
                spaceAfterColon = false,
                spaceBeforeAndAfterSpace = false,
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
}
