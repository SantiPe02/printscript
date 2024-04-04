package linterRules

import ast.AST
import result.validation.ValidationResult


sealed interface LinterRule{
    fun ruleIsValid(tree: AST): ValidationResult
}

