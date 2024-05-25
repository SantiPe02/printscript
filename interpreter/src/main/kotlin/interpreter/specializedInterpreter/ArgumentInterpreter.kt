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
    ): ComposedResult<Variable?> {
        return when (arg) {
            is LiteralArgument -> ComposedResult(Variable(arg.type, arg.value), interpreter)
            is MethodResult -> interpretMethodResult(interpreter, arg)
            is VariableArgument -> interpretVariable(interpreter, arg)
        }
    }

    fun interpretVariable(
        interpreter: Interpreter,
        expression: VariableArgument,
    ): ComposedResult<Variable?> {
        val variable: Variable? = interpreter.variables.get(expression.name)
        return if (variable == null) {
            ComposedResult(
                null,
                interpreter.reportError("variable ${expression.name} not declared in this scope. ${expression.range.start}"),
            )
        } else {
            ComposedResult(variable, interpreter)
        }
    }

    fun interpretMethodResult(
        interpreter: Interpreter,
        expression: MethodResult,
    ): ComposedResult<Variable?> {
        val result = CallInterpreter.interpret(interpreter, expression.methodCall)
        return if (result.data == null) {
            ComposedResult(null, result.interpreter)
        } else {
            interpret(result.interpreter, result.data)
        }
    }
}
