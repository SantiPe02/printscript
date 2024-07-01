package interpreter.specializedInterpreter

import ast.Argument
import ast.Call
import interpreter.Interpreter

object CallInterpreter {
    private var isInitialized = false
    private lateinit var printScriptFunctions: Map<String, PrintScriptMethod>

    fun initialize(functions: Map<String, PrintScriptMethod>) {
        printScriptFunctions = functions
        isInitialized = true
    }

    fun interpret(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?> {
        if (!isInitialized) throw IllegalStateException("CallInterpreter not initialized")
        val method: PrintScriptMethod =
            printScriptFunctions[sentence.name] ?: return ComposedResult(
                null,
                interpreter.reportError("the method ${sentence.name} is not a typescript method"),
            )
        return method.execute(interpreter, sentence)
    }
}

interface PrintScriptMethod {
    fun execute(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?>
}
