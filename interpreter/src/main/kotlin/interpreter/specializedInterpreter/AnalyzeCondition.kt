package interpreter.specializedInterpreter

import ast.BooleanCondition
import ast.Condition
import interpreter.Interpreter

public fun analyzeCondition(
    interpreter: Interpreter,
    condition: Condition,
): Result<Boolean> {
    when (condition) {
        is BooleanCondition -> {
            val variable =
                ArgumentInterpreter.interpret(interpreter, condition.argument).getOrElse {
                    return Result.failure(it)
                }
            if (!variable.type.equals("boolean")) {
                return Result.failure(Exception("argument was not boolean at ${condition.range}"))
            }
            return Result.success(variable.value == "true")
        }
    }
}
