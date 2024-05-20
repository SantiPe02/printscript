package interpreter.specializedInterpreter.printscriptMethods

import ast.Argument
import ast.Call
import ast.LiteralArgument
import ast.Range
import interpreter.Interpreter
import interpreter.Variable
import interpreter.specializedInterpreter.ArgumentInterpreter
import interpreter.specializedInterpreter.ComposedResult
import interpreter.specializedInterpreter.PrintScriptMethod

internal class BinaryOperation(val operation: (Double, Double) -> Double) : PrintScriptMethod {
    override fun execute(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?> {
        if (sentence.arguments.size != 2) {
            return ComposedResult(
                null,
                interpreter.reportError("operator ${sentence.name} in ${sentence.range} works only with 2 arguments"),
            )
        }
        val listResult = getArgumentsAsLiterals(sentence.arguments, interpreter)
        if (listResult.data == null) return ComposedResult(null, listResult.interpreter)

        val literals = listResult.data

        if (!areNumbers(literals[0], literals[1])) {
            ComposedResult(
                null,
                interpreter.reportError("operator ${sentence.name} in ${sentence.range} was expecting two numbers"),
            )
        }

        val literalResult = binaryOperation(literals[0], literals[1], operation)
        return ComposedResult(
            LiteralArgument(sentence.range, literalResult, "number"),
            interpreter,
        )
    }

    private fun binaryOperation(
        n: LiteralArgument,
        m: LiteralArgument,
        operation: (Double, Double) -> Double,
    ): String {
        val nValue = n.value.toDoubleOrNull()!!
        val mValue = m.value.toDoubleOrNull()!!
        return operation(nValue, mValue).toString()
    }
}

internal object AddOperation : PrintScriptMethod {
    override fun execute(
        interpreter: Interpreter,
        sentence: Call,
    ): ComposedResult<Argument?> {
        if (sentence.arguments.size != 2) {
            return ComposedResult(
                null,
                interpreter.reportError("operator ${sentence.name} in ${sentence.range} works only with 2 arguments"),
            )
        }
        val listResult = getArgumentsAsLiterals(sentence.arguments, interpreter)
        if (listResult.data == null) return ComposedResult(null, listResult.interpreter)

        val literals = listResult.data

        if (areNumbers(literals[0], literals[1])) {
            return BinaryOperation { a, b -> a + b }.execute(interpreter, sentence)
        }

        if (!oneIsString(literals[0], literals[1])) {
            return ComposedResult(
                null,
                interpreter.reportError(
                    "operator ${sentence.name} in ${sentence.range} " +
                        "does not know how to add ${literals[0].type} to ${literals[1].type}",
                ),
            )
        }
        val literalResult = addArgsAsStrings(literals[0], literals[1])
        return ComposedResult(
            LiteralArgument(sentence.range, literalResult, "string"),
            interpreter,
        )
    }

    private fun oneIsString(
        arg1: LiteralArgument,
        arg2: LiteralArgument,
    ): Boolean = arg1.type == "string" || arg2.type == "string"

    private fun addArgsAsStrings(
        arg1: LiteralArgument,
        arg2: LiteralArgument,
    ): String = arg1.value + arg2.value
}

private fun getArgumentsAsLiterals(
    args: Collection<Argument>,
    interpreter: Interpreter,
): ComposedResult<List<LiteralArgument>?> {
    val literals = mutableListOf<LiteralArgument>()
    var actualInterpreter = interpreter
    for (argument in args) {
        val result = ArgumentInterpreter.interpret(actualInterpreter, argument)
        if (result.data == null) {
            return ComposedResult(null, result.interpreter)
        }
        literals.add(variableToLiteral(result.data))
        actualInterpreter = result.interpreter
    }
    return ComposedResult(literals.toList(), actualInterpreter)
}

private fun variableToLiteral(variable: Variable): LiteralArgument {
    return LiteralArgument(Range(0, 0), variable.value!!, variable.type)
}

private fun areNumbers(
    arg1: LiteralArgument,
    arg2: LiteralArgument,
): Boolean = arg1.type == "number" && arg2.type == "number"
