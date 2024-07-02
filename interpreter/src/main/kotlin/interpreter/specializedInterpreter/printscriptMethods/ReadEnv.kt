package interpreter.specializedInterpreter.printscriptMethods

import ast.Argument
import ast.Call
import ast.LiteralArgument
import interpreter.Interpreter
import interpreter.specializedInterpreter.ArgumentInterpreter
import interpreter.specializedInterpreter.ComposedResult
import interpreter.specializedInterpreter.PrintScriptMethod

object ReadEnv : PrintScriptMethod {
    override fun execute(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?> {
        if (sentence.arguments.size != 1) {
            ComposedResult(
                null,
                interpreter.reportError("the method readEnv requires one argument at ${sentence.range}"),
            )
        }
        val argInterpretation = ArgumentInterpreter.interpret(interpreter, sentence.arguments.first())
        if (argInterpretation.data == null) {
            return ComposedResult(
                null,
                argInterpretation.interpreter.reportError("readEnv requires at least one value on ${sentence.range}"),
            )
        }

        return ComposedResult(
            LiteralArgument(sentence.range, System.getenv(argInterpretation.data.value), "string"),
            argInterpretation.interpreter,
        )
    }
}
