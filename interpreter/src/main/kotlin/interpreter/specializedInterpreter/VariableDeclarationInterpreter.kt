package interpreter.specializedInterpreter

import ast.VariableDeclaration
import interpreter.Interpreter
import interpreter.SpecializedInterpreter

object VariableDeclarationInterpreter : SpecializedInterpreter<VariableDeclaration> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: VariableDeclaration,
    ): Interpreter {
        return SetArgumentToVariable.set(
            interpreter,
            sentence.value,
            sentence.variableType,
            sentence.variableName,
            sentence.range,
        )
    }
}
