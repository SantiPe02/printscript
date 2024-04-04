package linterRules

import ast.AST
import ast.Declaration
import ast.DeclarationStatement
import result.validation.ValidResult
import result.validation.ValidationResult

class UndeclaratedVariableStatementRule : LinterRule {
    override fun ruleIsValid(tree: AST): ValidationResult {
        return if(tree is DeclarationStatement)
            ValidResult()
        else
            ValidResult()
    }

}