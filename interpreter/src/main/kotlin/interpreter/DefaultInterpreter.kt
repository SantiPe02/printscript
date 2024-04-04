package interpreter

import ast.Argument
import ast.Call
import ast.LiteralArgument
import ast.MethodResult
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration

data class Variable(val type: String, val value: String?)

class DefaultInterpreter {
    private val outputs: MutableList<String> = mutableListOf()
    private val errors: MutableList<String> = mutableListOf()
    val variables: MutableMap<String, Variable> = mutableMapOf()

    fun interpret(ast: Scope): Report {
        for (sentence in ast.body) {
            if (errors.isNotEmpty()) break
            when (sentence) {
                is LiteralArgument -> TODO()
                is MethodResult -> TODO()
                is VariableArgument -> TODO()
                is Call -> interpret(sentence)
                is VariableDeclaration -> interpret(sentence)
                is Scope -> TODO()
                else -> {}
            }
        }

        return Report(outputs, errors)
    }

    private fun interpret(sentence: Call) {
        val value =
            when (val argument: Argument = sentence.arguments.first()) {
                is MethodResult -> interpretArgument(argument)
                is LiteralArgument -> argument
                is VariableArgument -> interpretArgument(argument)
            }

        when (sentence.name) {
            "println" -> outputs.add(value.value)
        }
    }

    private fun interpret(sentence: VariableDeclaration) {
        val value: LiteralArgument =
            when (sentence.value) {
                is MethodResult -> interpretArgument(sentence.value as MethodResult)
                is LiteralArgument -> sentence as LiteralArgument
                is VariableArgument -> interpretArgument(sentence.value as VariableArgument)
            }

        variables[sentence.variableName] = Variable(sentence.variableType, value.value)
    }

    private fun interpretArgument(expression: VariableArgument): LiteralArgument {
        val value: Variable = variables[expression.name]!!

        return LiteralArgument(
            expression.range,
            value.value!!,
            value.type,
        )
    }

    private fun interpretArgument(expression: MethodResult): LiteralArgument {
        val literals = mutableListOf<LiteralArgument>()

        for (argument in expression.methodCall.arguments) {
            literals.add(
                when (argument) {
                    is MethodResult -> interpretArgument(argument)
                    is LiteralArgument -> argument
                    is VariableArgument -> interpretArgument(argument)
                },
            )
        }

        if (matchTypes(literals[0], literals[1], false)) {
            return when (expression.methodCall.name) {
                "*" ->
                    LiteralArgument(
                        expression.range,
                        binaryOperation(literals[0], literals[1]) { a, b -> a * b },
                        literals[0].type,
                    )

                "+" ->
                    LiteralArgument(
                        expression.range,
                        binaryOperation(literals[0], literals[1]) { a, b -> a + b },
                        literals[0].type,
                    )

                "-" ->
                    LiteralArgument(
                        expression.range,
                        binaryOperation(literals[0], literals[1]) { a, b -> a - b },
                        literals[0].type,
                    )

                "/" ->
                    LiteralArgument(
                        expression.range,
                        binaryOperation(literals[0], literals[1]) { a, b -> a / b },
                        literals[0].type,
                    )

                else -> LiteralArgument(expression.range, literals[0].type, literals[0].value)
            }
        } else {
            return LiteralArgument(literals[0].range, literals[0].value + literals[1].value, "string")
        }
    }

    private fun binaryOperation(
        n: LiteralArgument,
        m: LiteralArgument,
        operation: (Double, Double) -> Double,
    ): String {
        val nValue = n.value.toDoubleOrNull()!!
        val mValue = m.value.toDoubleOrNull()!!
        return operation(nValue, mValue).toString()
    }

    private fun matchTypes(
        aLiteral: LiteralArgument,
        otherLiteral: LiteralArgument,
        warnings: Boolean = true,
    ): Boolean {
        if (aLiteral.type === otherLiteral.type) {
            return true
        } else if (warnings) {
            errors.add(PrintScriptError.TYPE_MISMATCH.msg)
        }
        return false
    }
}
