package ast

/**
 * AST is the Abstract Syntax Tree abstraction for all the data.
 * It counts with the basic information of an AST node.
 * @property range indicates the starting point and the ending point of the data contained
 */
sealed interface AST {
    val range: Range
}

class Range(val start: Int, val end: Int) {
    override fun toString(): String {
        return "Range(start=$start, end=$end)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Range) return false

        return this.start == other.start && this.end == other.end
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }
}

/**
 * @param type: refers to the scale of the scope: program, package, file, class, method
 * @param body: the things that the scope is composed of.
 */
class Scope(
    val type: String,
    override val range: Range,
    val body: Collection<AST>,
) : AST {
    override fun toString(): String {
        return "Scope(type='$type', range=$range, body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Scope) return false

        if (type != other.type) return false
        if (range != other.range) return false
        if (body.size != other.body.size) return false

        val thisBodyIterator = body.iterator()
        val otherBodyIterator = other.body.iterator()
        while (thisBodyIterator.hasNext() && otherBodyIterator.hasNext()) {
            if (thisBodyIterator.next() != otherBodyIterator.next()) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + range.hashCode()
        result = 31 * result + body.hashCode()
        return result
    }
}

/**
 * Contains information required for the call of a method.
 * @param range: the range of the call, from the start of first argument to the end of the last.
 * @param name: the identifier of the method.
 * @param arguments: the values that the method is going to get.
 */
class Call(
    override val range: Range,
    val name: String,
    val arguments: Collection<Argument>,
) : AST {
    override fun toString(): String {
        return "Call(range=$range, name='$name', arguments=$arguments)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Call) return false

        if (range != other.range) return false
        if (name != other.name) return false
        if (arguments != other.arguments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + arguments.hashCode()
        return result
    }
}

/**
 * Used to contain data of declarations.
 * There are different type of declarations
 */
sealed interface Declaration : AST

/**
 * class with the information for variable declarations.
 * @param variableName: the name of the variable being declared
 * @param variableType: the type of the variable being declared (number string or any class)
 * @param value: the value the variable will take at the moment of being initialized
 */
class VariableDeclaration(
    override val range: Range,
    val variableName: String,
    val variableType: String,
    val value: Argument,
) : Declaration {
    override fun toString(): String {
        return "VariableDeclaration(range=$range, variableName='$variableName', variableType='$variableType', value=$value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VariableDeclaration) return false

        if (range != other.range) return false
        if (variableName != other.variableName) return false
        if (variableType != other.variableType) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + variableName.hashCode()
        result = 31 * result + variableType.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

class ConstantDeclaration(
    override val range: Range,
    val variableName: String,
    val variableType: String,
    val value: Argument,
) : Declaration {
    override fun toString(): String {
        return "ConstantDeclaration(range=$range, variableName='$variableName', variableType='$variableType', value=$value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConstantDeclaration) return false

        if (range != other.range) return false
        if (variableName != other.variableName) return false
        if (variableType != other.variableType) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + variableName.hashCode()
        result = 31 * result + variableType.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

sealed interface Argument : AST

class LiteralArgument(override val range: Range, val value: String, val type: String) : Argument {
    override fun toString(): String {
        return "LiteralArgument(range=$range, value='$value', type='$type')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteralArgument) return false

        if (range != other.range) return false
        if (value != other.value) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}

class VariableArgument(override val range: Range, val name: String) : Argument {
    override fun toString(): String {
        return "VariableArgument(range=$range, name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VariableArgument) return false

        if (range != other.range) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}

/**
 *  @param range:  range of the operator, in 5 + 3 range is Range(2,2)
 *  */
class MethodResult(override val range: Range, val methodCall: Call) : Argument {
    override fun toString(): String {
        return "MethodResult(range=$range, methodCall=$methodCall)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MethodResult) return false

        if (range != other.range) return false
        if (methodCall != other.methodCall) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + methodCall.hashCode()
        return result
    }
}

class DeclarationStatement(override val range: Range, val variableName: String, val variableType: String) : Declaration {
    override fun toString(): String {
        return "DeclarationStatement(range=$range, variableName='$variableName', variableType='$variableType')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DeclarationStatement) return false

        if (range != other.range) return false
        if (variableName != other.variableName) return false
        if (variableType != other.variableType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + variableName.hashCode()
        result = 31 * result + variableType.hashCode()
        return result
    }
}

class AssignmentStatement(override val range: Range, val variableName: String, val value: Argument) : Declaration {
    override fun toString(): String {
        return "AssignmentStatement(range=$range, variableName='$variableName', value=$value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return false
        if (other !is AssignmentStatement) return false

        if (range != other.range) return false
        if (variableName != other.variableName) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + variableName.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

sealed interface Condition : AST

/**
 * A simple condition only takes one boolean argument, e.g.: if(a)
 * */
class BooleanCondition(
    override val range: Range,
    val argument: Argument,
) : Condition {
    override fun toString(): String {
        return "BooleanCondition(range=$range, argument=$argument)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BooleanCondition) return false

        if (range != other.range) return false
        if (argument != other.argument) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + argument.hashCode()
        return result
    }
}

sealed interface Conditional : AST

class IfStatement(override val range: Range, val conditions: Collection<Condition>, val scope: Scope) : Conditional {
    override fun toString(): String {
        return "IfStatement(range=$range, conditions=$conditions, scope=$scope)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IfStatement) return false

        if (range != other.range) return false
        if (conditions != other.conditions) return false
        if (scope != other.scope) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + conditions.hashCode()
        result = 31 * result + scope.hashCode()
        return result
    }
}

class IfAndElseStatement(
    override val range: Range,
    val conditions: Collection<Condition>,
    val ifScope: Scope,
    val elseScope: Scope,
) : Conditional {
    override fun toString(): String {
        return "IfAndElseStatement(range=$range, conditions=$conditions, ifScope=$ifScope, elseScope=$elseScope)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IfAndElseStatement) return false

        if (range != other.range) return false
        if (conditions != other.conditions) return false
        if (ifScope != other.ifScope) return false
        if (elseScope != other.elseScope) return false

        return true
    }

    override fun hashCode(): Int {
        var result = range.hashCode()
        result = 31 * result + conditions.hashCode()
        result = 31 * result + ifScope.hashCode()
        result = 31 * result + elseScope.hashCode()
        return result
    }
}

/*/**
 * Comparison of two arguments, e.g.: if(a > 5)
 * */
class BinaryCondition(override val range: Range, val left: Argument, val operator: String, val right: Argument) : Condition{}

/**Conditional example:
 * if((i > 5) and (f)):
 *  --> IfStatement(
 *          range,
 *          BinaryCondition(
 *              BinaryCondition(
 *                  variableArgument(i), BIGGER, LiteralArgument(5)),
 *                  AND,
 *                  BooleanCondition(VariableArgument(f)
 *                  )
 *              )
 *       )
 */


sealed interface Conditional : AST


// puede ser null o result.
class IfStatement(override val range: Range, val conditions: Collection<Condition>, val body: Collection<AST>, val next: LinkedConditional?) : Conditional



sealed interface LinkedConditional : Conditional

// this is very repeated.
class IfElseStatement(override val range: Range, val conditions: Collection<Condition>, val body: Collection<AST>, val next: LinkedConditional?) : LinkedConditional {}

class ElseStatement(override val range: Range, val body: Collection<AST>) : LinkedConditional {}*/
