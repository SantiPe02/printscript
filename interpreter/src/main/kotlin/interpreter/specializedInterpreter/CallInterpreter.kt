package interpreter.specializedInterpreter

import ast.Argument
import ast.Call
import ast.LiteralArgument
import ast.MethodResult
import ast.VariableArgument
import interpreter.InputReader
import interpreter.Interpreter
import interpreter.Report

object CallInterpreter {
    fun interpret(
        interpreter: Interpreter,
        sentence: Call,
        reader: InputReader,
    ): Interpreter {
        val value =
            when (val argument: Argument = sentence.arguments.first()) {
                is MethodResult -> ArgumentInterpreter.interpretMethodResult(interpreter, argument)
                is LiteralArgument -> argument
                is VariableArgument -> ArgumentInterpreter.interpretVariable(interpreter, argument)
            }

        return when (sentence.name) {
            "println" -> Interpreter(Report(interpreter.report.outputs + value.value, interpreter.report.errors), interpreter.variables)
            "readInput" -> {
                val result = reader.readInput()
                Interpreter(Report(interpreter.report.outputs, interpreter.report.errors), interpreter.variables, result.reader)
            }
            else -> interpreter.reportError("The interpreter only manages println calls")
        }
    }
}
