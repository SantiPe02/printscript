package interpreter.specializedInterpreter

import ast.IfAndElseStatement
import interpreter.Interpreter
import interpreter.SpecializedInterpreter

object IfAndElseStatementInterpreter : SpecializedInterpreter<IfAndElseStatement> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: IfAndElseStatement,
    ): Interpreter {
        val condition = analyzeCondition(interpreter, sentence.conditions.first())
        if (condition.data == null) return interpreter

        return if (condition.data) {
            interpreter.interpret(sentence.ifScope)
        } else {
            interpreter.interpret(sentence.elseScope)
        }
    }
}
