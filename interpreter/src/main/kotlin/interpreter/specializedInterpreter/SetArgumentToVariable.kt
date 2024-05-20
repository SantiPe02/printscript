package interpreter.specializedInterpreter

import ast.Argument
import ast.Range
import interpreter.Interpreter
import interpreter.Variable

object SetArgumentToVariable {
    fun set(
        interpreter: Interpreter,
        argument: Argument,
        variableType: String,
        variableName: String,
        range: Range,
    ): Interpreter {
        val arg = ArgumentInterpreter.interpret(interpreter, argument)
        if (arg.data == null) return arg.interpreter

        if (variableType != arg.data.type) {
            if (arg.data.type != "string") {
                val error =
                    "miss mach type on runtime on $range." +
                        " was expecting $variableType and got ${arg.data.type}"
                return arg.interpreter.reportError(error)
            }
            try {
                if (variableType == "number") {
                    arg.data.value!!.toBigDecimal()
                } else if (variableType == "boolean") {
                    arg.data.value!!.toBooleanStrict()
                }
            } catch (e: Exception) {
                val error =
                    "miss mach type on runtime on $range." +
                        " was expecting $variableType and got ${arg.data.type}"
                return arg.interpreter.reportError(error)
            }
        }

        return Interpreter(
            arg.interpreter.report,
            arg.interpreter.variables + (
                variableName to
                    Variable(
                        variableType,
                        arg.data.value,
                        arg.data.constant,
                    )
            ),
            arg.interpreter.inputReader,
        )
    }
}
