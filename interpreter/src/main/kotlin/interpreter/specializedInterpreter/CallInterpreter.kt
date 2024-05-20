package interpreter.specializedInterpreter

import ast.Argument
import ast.Call
import interpreter.Interpreter
import interpreter.specializedInterpreter.printscriptMethods.AddOperation
import interpreter.specializedInterpreter.printscriptMethods.BinaryOperation
import interpreter.specializedInterpreter.printscriptMethods.PrintLine
import interpreter.specializedInterpreter.printscriptMethods.ReadInput

object CallInterpreter {
    private val printScriptFunctions: Map<String, PrintScriptMethod> =
        mapOf(
            ("println" to PrintLine),
            ("readInput" to ReadInput),
            ("+" to AddOperation),
            ("-" to BinaryOperation { a, b -> a - b }),
            ("*" to BinaryOperation { a, b -> a * b }),
            ("/" to BinaryOperation { a, b -> a / b }),
        )

    fun interpret(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?> {
        val method: PrintScriptMethod =
            printScriptFunctions[sentence.name] ?: return ComposedResult(
                null,
                interpreter.reportError("the method ${sentence.name} is not a typescript method"),
            )
        return method.execute(interpreter, sentence)
    }
}

internal interface PrintScriptMethod {
    fun execute(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?>
}
