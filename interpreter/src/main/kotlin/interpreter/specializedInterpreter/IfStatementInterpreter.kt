package interpreter.specializedInterpreter

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
}
