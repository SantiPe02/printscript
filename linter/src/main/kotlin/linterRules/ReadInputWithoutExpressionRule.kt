package linterRules

import ast.AST
import ast.Scope
import result.validation.ValidationResult

class ReadInputWithoutExpressionRule : LinterRule {
    override fun ruleIsValid(
        scope: Scope,
        tree: AST,
    ): ValidationResult {
        return FunctionCallWithoutExpressionRule().validate(tree, "readInput")
    }
}
