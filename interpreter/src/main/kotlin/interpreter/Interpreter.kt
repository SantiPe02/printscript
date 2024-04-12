package interpreter

import ast.Argument
import ast.AssignmentStatement
import ast.Call
import ast.DeclarationStatement
import ast.LiteralArgument
import ast.MethodResult
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration

data class Variable(val type: String, val value: String?)

data class Report(val outputs: List<String>, val errors: List<String>)

// TODO: use this, but add the range to the report of the error.
enum class PrintScriptError(val msg: String) {
    VARIABLE_ALREADY_DECLARED("Can't declare an existing variable."),
    VARIABLE_NOT_DECLARED("Variable not declared."),
    VARIABLE_NOT_ASSIGNED("Variable has not assigned value."),
    TYPE_MISMATCH("Variable and assigned value types mismatch."),
    OPERATION_NOT_ALLOWED("Operation can't be applied with this types."),
}

class Interpreter {
    private val outputs: MutableList<String> = mutableListOf()
    private val errors: MutableList<String> = mutableListOf()
    val variables: MutableMap<String, Variable> = mutableMapOf()

    fun interpret(ast: Scope): Report {
        for (sentence in ast.body) {
            if (errors.isNotEmpty()) break
            when (sentence) {
                is LiteralArgument -> TODO()
                is MethodResult -> interpret(sentence.methodCall)
                is VariableArgument -> TODO()
                is Call -> interpret(sentence)
                is VariableDeclaration -> interpret(sentence)
                is Scope -> TODO()
                is AssignmentStatement -> interpret(sentence)
                is DeclarationStatement -> interpret(sentence)
            }
        }

        return Report(outputs, errors)
    }

    private fun interpret(sentence: AssignmentStatement) {
        val variable: Variable? = variables.get(sentence.variableName)
        if (variable == null) {
            errors.add("variable ${sentence.variableName} not declared in this scope. ${sentence.range.start}")
            return
        }
        interpret(sentence.value).onFailure {
            errors.add("variable ${sentence.variableName} not declared in this scope. ${sentence.range.start}")
            return
        }.onSuccess {
            if (variable.type != it.type) {
                errors.add(
                    "miss mach type on runtime on ${sentence.range.start}." +
                        " was expecting ${variable.type} and got ${it.type}",
                )
                return
            }
            variables.replace(sentence.variableName, it)
        }
    }

    private fun interpret(sentence: DeclarationStatement) {
        if (variables.containsKey(sentence.variableName)) {
            errors.add("variable ${sentence.variableName} already declared in this scope. ${sentence.range.start}")
            return
        }
        variables.put(
            sentence.variableName,
            Variable(
                sentence.variableType,
                getDefaultValueForType(sentence.variableType),
            ),
        )
    }

    private fun getDefaultValueForType(type: String): String {
        return when (type) {
            "number" -> "0"
            "string" -> ""
            else -> "null"
        }
    }

    private fun interpret(sentence: Argument): Result<Variable> {
        return when (sentence) {
            is LiteralArgument -> Result.success(Variable(sentence.type, sentence.value))
            is MethodResult -> interpret(interpretArgument(sentence))
            is VariableArgument -> {
                val variable: Variable? = variables.get(sentence.name)
                if (variable == null) {
                    Result.failure(Error("variable ${sentence.name} not declared in this scope. ${sentence.range.start}"))
                } else {
                    Result.success(variable)
                }
            }
        }
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
            when (val value = sentence.value) {
                is MethodResult -> interpretArgument(value)
                is LiteralArgument -> value
                is VariableArgument -> interpretArgument(value)
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
        if (aLiteral.type == otherLiteral.type) {
            return true
        } else if (warnings) {
            errors.add(PrintScriptError.TYPE_MISMATCH.msg)
        }
        return false
    }
}
