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
class Scope(val type: String, override val range: Range, val body: Collection<AST>) : AST {
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
class Call(override val range: Range, val name: String, val arguments: Collection<Argument>) : AST {
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

sealed interface Argument : AST

class LiteralArgument(override val range: Range, val value: String, val type: String) : Argument {
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

// Range: range of the operator, in 5 + 3 range is Range(2,2)
class MethodResult(override val range: Range, val methodCall: Call) : Argument {
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
