package interpreter.specializedInterpreter

import ast.BooleanCondition
import ast.Condition
import ast.IfStatement
import interpreter.Interpreter
import interpreter.SpecializedInterpreter

object IfStatementInterpreter : SpecializedInterpreter<IfStatement> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: IfStatement,
    ): Interpreter {
        val condition =
            analyzeCondition(interpreter, sentence.conditions.first()).getOrElse {
                return interpreter.reportError(it.message ?: "")
            }
        if (condition) return interpreter.interpret(sentence.scope)
        return interpreter
    }

    private fun analyzeCondition(
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
}
