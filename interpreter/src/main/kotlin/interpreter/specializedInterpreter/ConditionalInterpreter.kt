package interpreter.specializedInterpreter

import ast.Conditional
import ast.IfAndElseStatement
import ast.IfStatement
import interpreter.Interpreter
import interpreter.SpecializedInterpreter

object ConditionalInterpreter : SpecializedInterpreter<Conditional> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: Conditional,
    ): Interpreter {
        return when (sentence) {
            is IfAndElseStatement -> IfAndElseStatementInterpreter.interpret(interpreter, sentence)
            is IfStatement -> IfStatementInterpreter.interpret(interpreter, sentence)
        }
    }
}
