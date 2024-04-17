package interpreter
import ast.AST
import ast.AssignmentStatement
import ast.Call
import ast.DeclarationStatement
import ast.LiteralArgument
import ast.MethodResult
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration
import interpreter.specializedInterpreter.AssignmentInterpreter
import interpreter.specializedInterpreter.CallInterpreter
import interpreter.specializedInterpreter.DeclarationInterpreter
import interpreter.specializedInterpreter.ScopeInterpreter
import interpreter.specializedInterpreter.VariableDeclarationInterpreter

data class Variable(val type: String, val value: String?)

data class Report(val outputs: List<String>, val errors: List<String>)

// TODO: use this, but add the range to the report of the error.
enum class PrintScriptError(val msg: String) {
    VARIABLE_ALREADY_DECLARED("Can't declare an existing variable."),
    VARIABLE_NOT_DECLARED("Variable not declared."),
    VARIABLE_NOT_ASSIGNED("Variable has not assigned value."),
    TYPE_MISMATCH("Variable and assigned value types mismatch."),
    OPERATION_NOT_ALLOWED("Operation can't be applied with this types."),
}

class Interpreter(
    val report: Report = Report(listOf(), listOf()),
    val variables: Map<String, Variable> = mapOf(),
) {
    fun interpret(sentence: AST): Interpreter =
        when (sentence) {
            is LiteralArgument -> TODO()
            is MethodResult -> interpret(sentence.methodCall)
            is VariableArgument -> TODO()
            is Call -> CallInterpreter.interpret(this, sentence)
            is VariableDeclaration -> VariableDeclarationInterpreter.interpret(this, sentence)
            is Scope -> ScopeInterpreter.interpret(this, sentence)
            is AssignmentStatement -> AssignmentInterpreter.interpret(this, sentence)
            is DeclarationStatement -> DeclarationInterpreter.interpret(this, sentence)
            else -> TODO()
        }

    fun reportError(error: String) = Interpreter(Report(report.outputs, report.errors + error), variables)
}
