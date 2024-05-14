package interpreter.specializedInterpreter

import ast.Scope
import interpreter.Interpreter
import interpreter.SpecializedInterpreter

object ScopeInterpreter : SpecializedInterpreter<Scope> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: Scope,
    ): Interpreter {
        var interpreterCopy = interpreter
        for (bodySentence in sentence.body) {
            if (interpreterCopy.report.errors.isNotEmpty()) return interpreterCopy
            interpreterCopy = interpreterCopy.interpret(bodySentence)
        }
        return interpreterCopy
    }
}
