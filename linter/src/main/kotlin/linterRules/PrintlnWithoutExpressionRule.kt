package linterRules

import ast.AST
import ast.Scope
import result.validation.ValidationResult

class PrintlnWithoutExpressionRule : LinterRule {
    override fun ruleIsValid(
        scope: Scope,
        tree: AST,
    ): ValidationResult {
        return FunctionCallWithoutExpressionRule().validate(tree, "println")
    }
}
