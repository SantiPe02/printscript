package interpreter.specializedInterpreter

import ast.AssignmentStatement
import interpreter.Interpreter
import interpreter.Report
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
        ArgumentInterpreter.interpret(interpreter, sentence.value).onFailure {
            val error = "variable ${sentence.variableName} not declared in this scope. ${sentence.range.start}"
            return interpreter.reportError(error)
        }.onSuccess {
            if (variable.type != it.type) {
                val error =
                    "miss mach type on runtime on ${sentence.range.start}." +
                        " was expecting ${variable.type} and got ${it.type}"
                return interpreter.reportError(error)
            }
            return Interpreter(interpreter.report, interpreter.variables + (sentence.variableName to it))
        }
        return Interpreter(Report(interpreter.report.outputs, interpreter.report.errors + "something went really wrong"))
    }
}
