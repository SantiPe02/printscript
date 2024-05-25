package interpreter.specializedInterpreter.printscriptMethods

import ast.Argument
import ast.Call
import ast.LiteralArgument
import ast.Range
import interpreter.Interpreter
import interpreter.Report
import interpreter.specializedInterpreter.ComposedResult
import interpreter.specializedInterpreter.PrintScriptMethod

internal object ReadInput : PrintScriptMethod {
    override fun execute(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?> {
        val interpreterWithOutput = PrintLine.execute(interpreter, sentence).interpreter
        val result = interpreterWithOutput.inputReader.readInput()
        val newI =
            Interpreter(
                Report(interpreterWithOutput.report.outputs, interpreterWithOutput.report.errors),
                interpreterWithOutput.variables,
                result.reader,
            )
        return ComposedResult(LiteralArgument(Range(0, 0), result.input, "string"), newI)
    }
}
