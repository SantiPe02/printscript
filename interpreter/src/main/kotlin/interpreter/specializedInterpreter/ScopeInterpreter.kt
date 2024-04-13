package interpreter.specializedInterpreter

import ast.Scope
import interpreter.Interpreter
import interpreter.SpecializedInterpreter

object ScopeInterpreter : SpecializedInterpreter<Scope> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: Scope,
    ): Interpreter {
        var interpreter = interpreter
        for (bodySentence in sentence.body) {
            if (interpreter.report.errors.isNotEmpty()) return interpreter
            interpreter = interpreter.interpret(bodySentence)
        }
        return interpreter
    }
}
