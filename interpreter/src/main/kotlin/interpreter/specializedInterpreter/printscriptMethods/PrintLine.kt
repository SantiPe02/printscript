package interpreter.specializedInterpreter.printscriptMethods

import ast.Argument
import ast.Call
import interpreter.Interpreter
import interpreter.Report
import interpreter.specializedInterpreter.ArgumentInterpreter
import interpreter.specializedInterpreter.ComposedResult
import interpreter.specializedInterpreter.PrintScriptMethod

internal object PrintLine : PrintScriptMethod {
    override fun execute(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?> {
        val argResult = ArgumentInterpreter.interpret(interpreter, sentence.arguments.first())
        if (argResult.data == null) {
            return ComposedResult(
                null,
                argResult.interpreter,
            )
        }
        val value = argResult.data
        return ComposedResult(
            null,
            Interpreter(
                Report(interpreter.report.outputs + value.value!!, interpreter.report.errors),
                interpreter.variables,
                interpreter.inputReader,
            ),
        )
    }
}
