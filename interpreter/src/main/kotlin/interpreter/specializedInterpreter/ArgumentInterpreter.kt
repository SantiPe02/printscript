package interpreter.specializedInterpreter

import ast.Argument
import ast.LiteralArgument
import ast.MethodResult
import ast.VariableArgument
import interpreter.Interpreter
import interpreter.Variable

object ArgumentInterpreter {
    fun interpret(
        interpreter: Interpreter,
        arg: Argument,
    ): Result<Variable> {
        return when (arg) {
            is LiteralArgument -> Result.success(Variable(arg.type, arg.value))
            is MethodResult -> interpret(interpreter, interpretMethodResult(interpreter, arg))
            is VariableArgument -> {
                val variable: Variable? = interpreter.variables.get(arg.name)
                if (variable == null) {
                    Result.failure(Error("variable ${arg.name} not declared in this scope. ${arg.range.start}"))
                } else {
                    Result.success(variable)
                }
            }
        }
    }

    fun interpretVariable(
        interpreter: Interpreter,
        expression: VariableArgument,
    ): LiteralArgument {
        val value: Variable = interpreter.variables[expression.name]!!

        return LiteralArgument(
            expression.range,
            value.value!!,
            value.type,
        )
    }

    fun interpretMethodResult(
        interpreter: Interpreter,
        expression: MethodResult,
    ): LiteralArgument {
        val literals = mutableListOf<LiteralArgument>()

        for (argument in expression.methodCall.arguments) {
            literals.add(
                when (argument) {
                    is MethodResult -> interpretMethodResult(interpreter, argument)
                    is LiteralArgument -> argument
                    is VariableArgument -> interpretVariable(interpreter, argument)
                },
            )
        }

        if (matchTypes(literals[0], literals[1])) {
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

    private fun matchTypes(
        aLiteral: LiteralArgument,
        otherLiteral: LiteralArgument,
    ): Boolean = aLiteral.type == otherLiteral.type

    private fun binaryOperation(
        n: LiteralArgument,
        m: LiteralArgument,
        operation: (Double, Double) -> Double,
    ): String {
        val nValue = n.value.toDoubleOrNull()!!
        val mValue = m.value.toDoubleOrNull()!!
        return operation(nValue, mValue).toString()
    }
}
