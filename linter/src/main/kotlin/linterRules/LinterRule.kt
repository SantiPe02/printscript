package linterRules

import ast.AST
import ast.Scope
import result.validation.ValidationResult


sealed interface LinterRule{
    fun ruleIsValid(scope: Scope, tree: AST): ValidationResult
}

