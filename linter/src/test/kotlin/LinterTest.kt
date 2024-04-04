import ast.MethodResult
import ast.Range
import ast.Scope
import ast.VariableDeclaration
import lexer.LexerImpl
import linterRules.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser
import result.validation.ValidResult
import result.validation.WarningResult

class LinterTest {
    @Test
    fun test001_testIsCamelCase() {
        val code = "let myVariable: int = 1;"
        val camelCaseRule = CamelCaseRule()
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast: Scope = parser.parseTokens(tokens) as Scope
        assertEquals(camelCaseRule.ruleIsValid(ast, ast.body.first()), ValidResult())
    }

    @Test
    fun test002_testIsNotCamelCase() {
        val code = "let my_variable: int = 1;" //¡Es un scope! El primer elemento del body es el variableDeclaration
        val camelCaseRule = CamelCaseRule()
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast: Scope = parser.parseTokens(tokens)
        println(ast.body.first()::class)
        assertEquals(camelCaseRule.ruleIsValid(ast, ast.body.first())::class, WarningResult::class)
    }

    @Test
    fun test003_testPrintlnWithoutExpression_notUsingExpression() {
        val code = "println(\"Hello, World!\");" // actually, creo que el parser todavía no soporta esto
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val rule = PrintlnWithoutExpressionRule()
        val first = ast.body.first() as MethodResult
        assertEquals(rule.ruleIsValid(ast, first.methodCall), ValidResult())
    }

    @Test
    fun test004_testPrintlnWithoutExpression_usingExpression() {
        val code = "println(\"Hello, World!\" + 1);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val rule = PrintlnWithoutExpressionRule()
        val first = ast.body.first() as MethodResult
        assertEquals(rule.ruleIsValid(ast, first.methodCall)::class, WarningResult::class)
    }

    @Test
    fun test005_testLinterWithCamelCaseRule(){
        val code = "let myVariable: int = 1; let my_variable: int = 1;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(CamelCaseRule())
        val wrongVar = ast.body.elementAtOrNull(1) as VariableDeclaration
        val expectedResult = listOf(WarningResult(Range(29, 39), "${wrongVar.variableName} is not in Camel Case"))
        assertEquals(linter.lintScope(ast, rules), expectedResult)
    }


    @Test
    fun test006_testLinterWithCamelCaseAndPrintlnRule(){
        val code = "let myVariable: int = 1; println(\"Hello, World!\" + 1);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(CamelCaseRule(), PrintlnWithoutExpressionRule())
        val expectedResult = listOf(WarningResult(Range(33, 51), "println should not have an expression inside it."))
        assertEquals(linter.lintScope(ast, rules), expectedResult)
    }

    @Test
    fun test007_UndeclaredVariableStatementRule_itIsNeverDeclared(){
        val code = "let myVariable: int;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(UndeclaratedVariableStatementRule())
        val expectedResult = listOf(WarningResult(Range(4, 13), "myVariable is never declared."))
        assertEquals(linter.lintScope(ast, rules), expectedResult)
    }
    @Test
    fun test008_UndeclaredVariableStatementRule_itIsDeclared(){
        val code = "let myVariable: int; myVariable = 1;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(UndeclaratedVariableStatementRule())
        val expectedResult = listOf<WarningResult>()
        assertEquals(linter.lintScope(ast, rules), expectedResult)
    }
}