package interpreter.specializedInterpreter

import ast.BooleanCondition
import ast.Condition
import interpreter.Interpreter

public fun analyzeCondition(
    interpreter: Interpreter,
    condition: Condition,
): ComposedResult<Boolean?> {
    when (condition) {
        is BooleanCondition -> {
            val variable = ArgumentInterpreter.interpret(interpreter, condition.argument)
            if (variable.data == null) return ComposedResult(null, interpreter)
            if (!variable.data.type.equals("boolean")) {
                return ComposedResult(
                    null,
                    variable.interpreter.reportError("argument was not boolean at ${condition.range}"),
                )
            }
            return ComposedResult(variable.data.value == "true", variable.interpreter)
        }
    }
}
