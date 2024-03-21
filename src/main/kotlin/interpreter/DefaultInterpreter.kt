package interpreter

import ast.*

data class Variable(val type: String, val value: String?)

class DefaultInterpreter {

    private val outputs: MutableList<String> = mutableListOf()
    private val errors: MutableList<String> = mutableListOf()
    val variables: MutableMap<String, Variable> = mutableMapOf()

    fun interpret(ast: Scope): Report {

        for (sentence in ast.body) {
            if (errors.isNotEmpty()) break
            when (sentence) {
                //is DeclareNode -> interpret(sentence)
                //is AssignNode -> interpret(sentence)
                //is DeclareAndAssignNode -> interpret(sentence)
                //is PrintNode -> interpret(sentence)
                is LiteralArgument -> TODO()
                is MethodResult -> TODO()
                is VariableArgument -> TODO()
                is Call -> TODO()
                is VariableDeclaration -> interpret(sentence)
                is Scope -> TODO()
            }
        }

        return Report(outputs, errors)
    }

    private fun interpret(sentence: VariableDeclaration) {
        val value = when (sentence.value) {
            is MethodResult -> interpretArgument(sentence.value)
            is LiteralArgument -> sentence.value
            is VariableArgument -> interpretArgument(sentence.value)
        }

        variables[sentence.variableName] = Variable(sentence.variableType, value.value)
    }

    private fun interpretArgument(expression: VariableArgument): LiteralArgument {
        val value: Variable = variables[expression.name]!!

        return LiteralArgument(
            expression.range,
            value.value!!,
            value.type
        )
    }

    private fun interpretArgument(expression: MethodResult): LiteralArgument {
        val literals = mutableListOf<LiteralArgument>()

        for (argument in expression.methodCall.arguments) {
            literals.add(when (argument) {
                is MethodResult -> interpretArgument(argument)
                is LiteralArgument -> argument
                is VariableArgument -> interpretArgument(argument)
            })
        }

        return when (expression.methodCall.name) {
            "*" -> LiteralArgument(expression.range, literals[0].type, (literals[0].value.toIntOrNull()!! * literals[1].value.toIntOrNull()!!).toString())
            "+" -> LiteralArgument(expression.range, literals[0].type, (literals[0].value.toIntOrNull()!! + literals[1].value.toIntOrNull()!!).toString())
            else -> LiteralArgument(expression.range, literals[0].type, literals[0].value)
        }
    }

    private fun resetScope() {
        outputs.clear()
        errors.clear()
        variables.clear()
    }
/*
    private fun interpret(declaration: DeclareNode) {
        val literal: Literal? = variables[declaration.identifier]
        if (literal === null)
            variables[declaration.identifier] = Literal(declaration.type, null)
        else errors.add(PrintScriptError.VARIABLE_ALREADY_DECLARED.msg)
    }
    private fun interpret(assignation: AssignNode) {
        val literal: Literal? = variables[assignation.identifier]
        val result: Literal? = interpretExpression(assignation.value)
        if (literal !== null)
            if (result !== null)
                if (matchTypes(literal, result))
                    variables[assignation.identifier] = Literal(literal.type, result.value)
                else errors.add(PrintScriptError.VARIABLE_NOT_DECLARED.msg)
    }
    private fun interpret(declareAndAssign: DeclareAndAssignNode) {
        interpret(declareAndAssign.declare)
        interpret(AssignNode(declareAndAssign.declare.identifier, declareAndAssign.value))
    }
    private fun interpret(printNode: PrintNode) {
        val result: Literal? = interpretExpression(printNode.argument)
        if (result !== null) outputs.add(result.value.toString())
    }

    private fun interpretExpression(expression: ExpressionNode): Literal? {
        return when (expression) {
            is LiteralNode -> Literal(expression.type, expression.value)
            is IdentifierNode -> solve(expression)
            is AdditionNode -> solve(expression)
            is SimpleBinaryOperation -> solve(expression)
            else -> null
        }
    }

    private fun solve(identifierNode: IdentifierNode): Literal? {
        val literal: Literal? = variables[identifierNode.identifier]
        if (literal !== null) {
            if (literal.value !== null) return literal
            else errors.add(PrintScriptError.VARIABLE_NOT_ASSIGNED.msg)
        }
        errors.add(PrintScriptError.VARIABLE_NOT_DECLARED.msg)
        return null
    }
    private fun solve(addition: AdditionNode): Literal? {
        val left = interpretExpression(addition.left)
        val right = interpretExpression(addition.right)

        if (left !== null && right !== null)
            return if (matchTypes(left, right, warnings = false)) {
                when (left.type) {
                    PrintScriptType.STRING -> Literal(left.type, left.value + right.value)
                    PrintScriptType.NUMBER -> Literal(left.type, binaryOperation(left, right) { a, b -> a + b })
                }
            } else Literal(PrintScriptType.STRING, left.value + right.value)
        return null
    }
    private fun solve(expression: SimpleBinaryOperation): Literal? {
        val left = interpretExpression(expression.left)
        val right = interpretExpression(expression.right)

        if (left !== null && right !== null) {
            if (left.type === PrintScriptType.NUMBER && right.type === PrintScriptType.NUMBER)
                return Literal(PrintScriptType.NUMBER, when (expression) {
                    is SubtractionNode -> binaryOperation(left, right) { a, b -> a - b }
                    is MultiplicationNode -> binaryOperation(left, right) { a, b -> a * b}
                    is DivisionNode -> binaryOperation(left, right) { a, b -> a / b}
                    else -> null
                })
            else errors.add(PrintScriptError.OPERATION_NOT_ALLOWED.msg)
        }
        return null
    }

    private fun matchTypes(aLiteral: Literal, otherLiteral: Literal, warnings: Boolean = true): Boolean {
        if (aLiteral.type === otherLiteral.type) return true
        else if (warnings) errors.add(PrintScriptError.TYPE_MISMATCH.msg)
        return false
    }

    private fun binaryOperation(n: Literal, m: Literal, operation: (Double, Double) -> Double): String {
        val nValue = n.value?.toDoubleOrNull()!!
        val mValue = m.value?.toDoubleOrNull()!!
        return operation(nValue, mValue).toString()
    }

 */
}