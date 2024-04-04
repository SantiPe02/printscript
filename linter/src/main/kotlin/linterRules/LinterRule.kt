package linterRules

import ast.AST
import ast.Scope
import result.validation.ValidationResult


sealed interface LinterRule{
    /**
     * @param scope: The entire scope that is being analysed.
     * @param tree: the current AST inside the scope that we are analyzing.
     */
    fun ruleIsValid(scope: Scope, tree: AST): ValidationResult
}

