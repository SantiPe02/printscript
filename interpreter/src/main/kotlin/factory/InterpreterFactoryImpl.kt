package factory

import interpreter.specializedInterpreter.CallInterpreter
import interpreter.specializedInterpreter.printscriptMethods.AddOperation
import interpreter.specializedInterpreter.printscriptMethods.BinaryOperation
import interpreter.specializedInterpreter.printscriptMethods.PrintLine
import interpreter.specializedInterpreter.printscriptMethods.ReadEnv
import interpreter.specializedInterpreter.printscriptMethods.ReadInput

class InterpreterFactoryImpl(val version: String) : InterpreterFactory {
    override fun create() {
        val v11functions =
            mapOf(
                ("println" to PrintLine),
                ("readInput" to ReadInput),
                ("readEnv" to ReadEnv),
                ("+" to AddOperation),
                ("-" to BinaryOperation { a, b -> a - b }),
                ("*" to BinaryOperation { a, b -> a * b }),
                ("/" to BinaryOperation { a, b -> a / b }),
            )
        val v10functions =
            mapOf(
                ("println" to PrintLine),
                ("+" to AddOperation),
                ("-" to BinaryOperation { a, b -> a - b }),
                ("*" to BinaryOperation { a, b -> a * b }),
                ("/" to BinaryOperation { a, b -> a / b }),
            )
        when (version) {
            "1.0" -> CallInterpreter.initialize(v10functions)
            "1.1" -> CallInterpreter.initialize(v11functions)
            else -> throw IllegalArgumentException("Unknown version")
        }
    }
}
