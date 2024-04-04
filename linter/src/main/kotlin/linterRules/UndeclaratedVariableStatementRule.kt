package linterRules

import ast.AST
import ast.AssignmentStatement
import ast.DeclarationStatement
import ast.Scope
import result.validation.ValidResult
import result.validation.ValidationResult
import result.validation.WarningResult

class UndeclaratedVariableStatementRule : LinterRule {
    override fun ruleIsValid(
        scope: Scope,
        tree: AST,
    ): ValidationResult {
        return if (tree is DeclarationStatement) {
            isVariableDeclared(scope, tree)
        } else {
            ValidResult()
        }
    }

    fun isVariableDeclared(
        scope: Scope,
        tree: DeclarationStatement,
    ): ValidationResult {
        val isDeclared =
            scope.body.any { declaration ->
                declaration is AssignmentStatement && declaration.variableName == tree.variableName
            }
        return if (isDeclared) {
            ValidResult()
        } else {
            WarningResult(tree.range, "${tree.variableName} is never declared.")
        }
    }
}
