package interpreter.specializedInterpreter

import ast.AssignmentStatement
import interpreter.Interpreter
import interpreter.SpecializedInterpreter
import interpreter.Variable

object AssignmentInterpreter : SpecializedInterpreter<AssignmentStatement> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: AssignmentStatement,
    ): Interpreter {
        val variable: Variable? = interpreter.variables.get(sentence.variableName)
        if (variable == null) {
            val error = "variable ${sentence.variableName} not declared in this scope. ${sentence.range.start}"
            return interpreter.reportError(error)
        }
        if (variable.constant) {
            val error = "variable ${sentence.variableName} is a constant and the value can't be changed. ${sentence.range.start}"
            return interpreter.reportError(error)
        }
        return SetArgumentToVariable.set(
            interpreter,
            sentence.value,
            variable.type,
            sentence.variableName,
            sentence.range,
        )
    }
}
