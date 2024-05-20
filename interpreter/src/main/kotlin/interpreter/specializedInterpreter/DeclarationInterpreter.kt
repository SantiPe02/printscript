package interpreter.specializedInterpreter

import ast.DeclarationStatement
import interpreter.Interpreter
import interpreter.SpecializedInterpreter
import interpreter.Variable

object DeclarationInterpreter : SpecializedInterpreter<DeclarationStatement> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: DeclarationStatement,
    ): Interpreter {
        if (interpreter.variables.containsKey(sentence.variableName)) {
            val error = "variable ${sentence.variableName} already declared in this scope. ${sentence.range.start}"
            return interpreter.reportError(error)
        }

        return Interpreter(
            interpreter.report,
            interpreter.variables +
                (
                    sentence.variableName to
                        Variable(
                            sentence.variableType,
                            getDefaultValueForType(sentence.variableType),
                        )
                ),
            interpreter.inputReader,
        )
    }

    private fun getDefaultValueForType(type: String): String {
        return when (type) {
            "number" -> "0"
            "string" -> ""
            else -> "null"
        }
    }
}
