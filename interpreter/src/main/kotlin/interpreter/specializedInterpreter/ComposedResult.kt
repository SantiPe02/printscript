package interpreter.specializedInterpreter

import interpreter.Interpreter

data class ComposedResult<T>(val data: T, val interpreter: Interpreter)
