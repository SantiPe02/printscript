package interpreter.specializedInterpreter

import ast.ConstantDeclaration
import interpreter.Interpreter
import interpreter.SpecializedInterpreter
import interpreter.Variable

object ConstantDeclarationInterpreter : SpecializedInterpreter<ConstantDeclaration> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: ConstantDeclaration,
    ): Interpreter {
        val argResult = ArgumentInterpreter.interpret(interpreter, sentence.value)
        if (argResult.data == null) return argResult.interpreter

        return Interpreter(
            interpreter.report,
            interpreter.variables + (
                sentence.variableName
                    to
                    Variable(sentence.variableType, argResult.data.value, true)
            ),
            interpreter.inputReader,
        )
    }
}
