package interpreter

interface SpecializedInterpreter<T> {
    fun interpret(
        interpreter: Interpreter,
        sentence: T,
    ): Interpreter
}
