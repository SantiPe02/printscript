package interpreter.specializedInterpreter

import ast.Argument
import ast.Call
import ast.LiteralArgument
import ast.MethodResult
import ast.VariableArgument
import interpreter.Interpreter
import interpreter.Report
import interpreter.SpecializedInterpreter

object CallInterpreter : SpecializedInterpreter<Call> {
    override fun interpret(
        interpreter: Interpreter,
        sentence: Call,
    ): Interpreter {
        val value =
            when (val argument: Argument = sentence.arguments.first()) {
                is MethodResult -> ArgumentInterpreter.interpretMethodResult(interpreter, argument)
                is LiteralArgument -> argument
                is VariableArgument -> ArgumentInterpreter.interpretVariable(interpreter, argument)
            }

        return when (sentence.name) {
            "println" -> Interpreter(Report(interpreter.report.outputs + value.value, interpreter.report.errors), interpreter.variables)
            else -> interpreter.reportError("The interpreter only manages println calls")
        }
    }
}
