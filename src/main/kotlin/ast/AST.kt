package ast

import org.intellij.lang.annotations.Identifier

/**
 * AST is the Abstract Syntax Tree abstraction for all the data.
 * It counts with the basic information of an AST node.
 * @property range indicates the starting point and the ending point of the data contained
 */
sealed interface AST {
    val range: Range
}
class Range(start: Long, end: Long)

/**
 * @param type: refers to the scale of the scope: program, package, file, class, method
 * @param body: the things that the scope is composed of.
 */
class Scope(val type: String, override val range: Range, val body: Collection<AST>) : AST

/**
 * Contains information required for the call of a method.
 * @param name: the identifier of the method.
 * @param arguments: the values that the method is going to get.
 */
class Call(override val range: Range, val name: String, val arguments: Collection<Argument>): AST

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
    val value: Argument
) : Declaration

sealed interface Argument: AST
class LiteralArgument(override val range: Range, val value: String, val type: String): Argument
class VariableArgument(override val range: Range,  val name: String): Argument
class MethodResult(override val range: Range, val methodCall: Call): Argument

/**
 * Proposal of AST, by Miguel
 * Need add Range to Nodes data
 */

enum class PrintScriptType {
    STRING,
    NUMBER
}

//TODO: merge both proposals, bringing out the best of each one.
data class AbstractSyntaxTree(val nodes: List<Node>)
abstract class Node

abstract class ExpressionNode: Node()
class LiteralNode(val type: PrintScriptType, val value: String): ExpressionNode()
class IdentifierNode(val identifier: String): ExpressionNode()
class AdditionNode(val left: ExpressionNode, val right: ExpressionNode): ExpressionNode()

abstract class SimpleBinaryOperation(val left: ExpressionNode, val right: ExpressionNode): ExpressionNode()
class SubtractionNode(left: ExpressionNode, right: ExpressionNode) : SimpleBinaryOperation(left, right)
class MultiplicationNode(left: ExpressionNode, right: ExpressionNode): SimpleBinaryOperation(left, right)
class DivisionNode(left: ExpressionNode, right: ExpressionNode): SimpleBinaryOperation(left, right)

class DeclareNode(val type: PrintScriptType, val identifier: String): Node()
class AssignNode(val identifier: String, val value: ExpressionNode): Node()
class DeclareAndAssignNode(val declare: DeclareNode, val value: ExpressionNode): Node()
class PrintNode(val argument: ExpressionNode): Node()