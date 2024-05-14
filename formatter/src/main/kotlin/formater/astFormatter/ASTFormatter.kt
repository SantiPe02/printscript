package formater.astFormatter

import ast.AST
import ast.Argument
import ast.AssignmentStatement
import ast.BooleanCondition
import ast.Call
import ast.Condition
import ast.Conditional
import ast.ConstantDeclaration
import ast.Declaration
import ast.DeclarationStatement
import ast.IfAndElseStatement
import ast.IfStatement
import ast.LiteralArgument
import ast.MethodResult
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration
import formater.FormatterConfiguration
import formater.IFormatter
import lexer.Lexer
import parser.Parser

class ASTFormatter(private val lexer: Lexer, private val parser: Parser, private val config: FormatterConfiguration) : IFormatter {
    override fun formatString(sourceCode: String): String {
        val ast = parser.parseTokens(lexer.tokenize(sourceCode)).getOrElse { throw Exception("Failed to build ast") }
        return processAST(ast)
    }

    fun processAST(ast: AST): String {
        return when (ast) {
            is Scope -> processScope(ast)
            is Call -> processCall(ast)
            is Argument -> processArgument(ast)
            is Declaration -> processDeclaration(ast)
            is Condition -> processCondition(ast)
            is Conditional -> processConditional(ast)
        }
    }

    private fun processDeclaration(declaration: Declaration): String {
        return when (declaration) {
            is DeclarationStatement -> {
                if (config.spaceAfterColon && config.spaceBeforeColon) {
                    declaration.variableName + " : " + declaration.variableType + ";"
                } else if (!config.spaceAfterColon && !config.spaceBeforeColon) {
                    declaration.variableName + ":" + declaration.variableType + ";"
                } else if (config.spaceAfterColon) {
                    declaration.variableName + ": " + declaration.variableType + ";"
                } else {
                    declaration.variableName + ": " + declaration.variableType + ";"
                }
            }
            is AssignmentStatement -> {
                if (config.spaceBeforeAndAfterEqual) {
                    declaration.variableName + " = " + processAST(declaration.value) + ";"
                } else {
                    declaration.variableName + "=" + processAST(declaration.value) + ";"
                }
            }
            is VariableDeclaration -> {
                val colon =
                    if (config.spaceBeforeColon && config.spaceAfterColon) {
                        " : "
                    } else if (config.spaceBeforeColon) {
                        " :"
                    } else if (config.spaceAfterColon) {
                        ": "
                    } else {
                        ":"
                    }
                val equals =
                    if (config.spaceBeforeAndAfterEqual) {
                        " = "
                    } else {
                        "="
                    }
                "let ${declaration.variableName}$colon${declaration.variableType}$equals${processAST(declaration.value)};"
            }

            is ConstantDeclaration -> TODO()
        }
    }

    private fun processArgument(argument: Argument): String {
        return when (argument) {
            is LiteralArgument -> {
                if (argument.type == "string") {
                    "\"" + argument.value + "\""
                } else {
                    argument.value
                }
            }
            is VariableArgument -> argument.name
            is MethodResult -> processAST(argument.methodCall)
        }
    }

    private fun processCall(call: Call): String {
        if (isOperator(call.name)) {
            return call.arguments.joinToString(" " + call.name + " ") { processAST(it) }
        }
        if (call.name == "println") {
            return "\n".repeat(
                config.lineJumpBeforePrintln,
            ) + call.name + "(" + call.arguments.joinToString(", ") { processAST(it) } + ")" + ";"
        }
        return call.name + "(" + call.arguments.joinToString(", ") { processAST(it) } + ")" + ";"
    }

    private fun isOperator(token: String): Boolean {
        return Regex("""[+-/*]""").matches(token)
    }

    private fun processScope(scope: Scope): String {
        return scope.body.joinToString("\n") { processAST(it) }
    }

    private fun processCondition(condition: Condition): String {
        return when (condition) {
            is BooleanCondition -> processAST(condition.argument)
        }
    }

    private fun processConditional(conditional: Conditional): String {
        return when (conditional) {
            is IfStatement -> "if (${conditional.conditions.joinToString(
                " && ",
            ) { processAST(it) }}) {\n${processAST(conditional.scope).prependIndent(indentation(config.indentation))}\n}"
            is IfAndElseStatement -> "if (${conditional.conditions.joinToString(
                " && ",
            ) {
                processAST(
                    it,
                )
            }}) {\n${processAST(
                conditional.ifScope,
            ).prependIndent(
                indentation(config.indentation),
            )}\n} else {\n${processAST(conditional.elseScope).prependIndent(indentation(config.indentation))}\n}"
        }
    }

    private fun indentation(numberOfSpaces: Int): String {
        return " ".repeat(numberOfSpaces)
    }
}
