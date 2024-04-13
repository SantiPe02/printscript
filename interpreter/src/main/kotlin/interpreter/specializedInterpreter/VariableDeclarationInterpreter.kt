package interpreter.specializedInterpreter

import ast.LiteralArgument
import ast.MethodResult
import ast.VariableArgument
import ast.VariableDeclaration
import interpreter.Interpreter
import interpreter.SpecializedInterpreter
import interpreter.Variable

object VariableDeclarationInterpreter : SpecializedInterpreter<VariableDeclaration> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: VariableDeclaration,
    ): Interpreter {
        val value: LiteralArgument =
            when (val value = sentence.value) {
                is MethodResult -> ArgumentInterpreter.interpretMethodResult(interpreter, value)
                is LiteralArgument -> value
                is VariableArgument -> ArgumentInterpreter.interpretVariable(interpreter, value)
            }

        return Interpreter(
            interpreter.report,
            interpreter.variables + (sentence.variableName to Variable(sentence.variableType, value.value)),
        )
    }
}
