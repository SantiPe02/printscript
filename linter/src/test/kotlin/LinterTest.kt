import ast.MethodResult
import ast.Range
import ast.VariableDeclaration
import factory.LexerFactoryImpl
import linterRules.CamelCaseRule
import linterRules.PrintlnWithoutExpressionRule
import linterRules.ReadInputWithoutExpressionRule
import linterRules.UndeclaratedVariableStatementRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser
import result.validation.ValidResult
import result.validation.WarningResult

class LinterTest {
    private val lexer = LexerFactoryImpl("1.1").create()

    @Test
    fun test001_testIsCamelCase() {
        val code = "let myVariable: number = 1;"
        val camelCaseRule = CamelCaseRule()
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            assertEquals(camelCaseRule.ruleIsValid(it, it.body.first()), ValidResult())
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun test002_testIsNotCamelCase() {
        val code = "let my_variable: number = 1;" // ¡Es un scope! El primer elemento del body es el variableDeclaration
        val camelCaseRule = CamelCaseRule()
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            assertEquals(camelCaseRule.ruleIsValid(it, it.body.first())::class, WarningResult::class)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun test003_testPrintlnWithoutExpression_notUsingExpression() {
        val code = "println(\"Hello, World!\");" // actually, creo que el parser todavía no soporta esto
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val rule = PrintlnWithoutExpressionRule()
            val first = it.body.first() as MethodResult
            assertEquals(rule.ruleIsValid(it, first.methodCall), ValidResult())
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun test004_testprintlnWithoutExpression_usingExpression() {
        val code = "println(\"Hello, World!\" + 1);"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val rule = PrintlnWithoutExpressionRule()
            val first = it.body.first() as MethodResult
            assertEquals(rule.ruleIsValid(it, first.methodCall)::class, WarningResult::class)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun test005_testlinterWithCamelCaseRule() {
        val code = "let myVariable: number = 1; let my_variable: number = 1;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(CamelCaseRule())
        ast.onSuccess {
            val wrongVar = it.body.elementAtOrNull(1) as VariableDeclaration
            val expectedResult = listOf(WarningResult(Range(32, 42), "${wrongVar.variableName} is not in Camel Case"))
            assertEquals(linter.lintScope(it, rules), expectedResult)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun test006_testlinterWithCamelCaseAndPrintlnRule() {
        val code = "let myVariable: number = 1; println(\"Hello, World!\" + 1);"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(CamelCaseRule(), PrintlnWithoutExpressionRule())
        ast.onSuccess {
            val expectedResult =
                listOf(WarningResult(Range(36, 54), "println should not have an expression inside it."))
            assertEquals(linter.lintScope(it, rules), expectedResult)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun test007_UndeclaredVariableStatementRule_itIsNeverDeclared() {
        val code = "let myVariable: number;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(UndeclaratedVariableStatementRule())
        ast.onSuccess {
            val expectedResult = listOf(WarningResult(Range(4, 13), "myVariable is never declared."))
            assertEquals(linter.lintScope(it, rules), expectedResult)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun test008_UndeclaredVariableStatementRule_itIsDeclared() {
        val code = "let myVariable: number; myVariable = 1;"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(UndeclaratedVariableStatementRule())
        val expectedResult = listOf<WarningResult>()
        ast.onSuccess {
            assertEquals(linter.lintScope(it, rules), expectedResult)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun `test009 test readInput with a sum expression inside should return warning`() {
        val code = "readInput(1 + 4);"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val rule = ReadInputWithoutExpressionRule()
            val first = it.body.first() as MethodResult
            assertEquals(rule.ruleIsValid(it, first.methodCall)::class, WarningResult::class)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun `test010 test readInput with a method expression inside should return warning`() {
        val code = "readInput(boque());"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val rule = ReadInputWithoutExpressionRule()
            println(it.body.first().javaClass)
            val first = it.body.first() as MethodResult
            assertEquals(rule.ruleIsValid(it, first.methodCall)::class, WarningResult::class)
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun `test011 test readInput without an expression inside should not return warning`() {
        val code = "readInput(1);"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        ast.onSuccess {
            val rule = ReadInputWithoutExpressionRule()
            val first = it.body.first() as MethodResult
            assertEquals(rule.ruleIsValid(it, first.methodCall), ValidResult())
        }.onFailure { fail("ast could not be successfully created") }
    }

    @Test
    fun `test009 test ReadEnv inside println should return warning`() {
        val code = "println(readEnv(a));"
        val tokens = lexer.tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val linter = MyLinter()
        val rules = listOf(PrintlnWithoutExpressionRule())
        ast.onSuccess {
            val expectedResult = listOf(WarningResult(Range(8, 17), "println should not have an expression inside it."))
            assertEquals(linter.lintScope(it, rules), expectedResult)
        }
    }
}
