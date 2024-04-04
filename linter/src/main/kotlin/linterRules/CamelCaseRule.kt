package linterRules

import ast.AST
import ast.Declaration
import ast.Range
import ast.VariableDeclaration
import result.validation.ValidResult
import result.validation.ValidationResult
import result.validation.WarningResult

// Only declarations (classes, methods, variables) must follow this rule. And, since they are only the length of one word,
// the rule is quite simple.

class CamelCaseRule:LinterRule {
    override fun ruleIsValid(tree: AST): ValidationResult {
        return if(tree is Declaration)
            isCamelCase(tree)
        else
            ValidResult()
    }

    // todo in the future when we make method or class declarations.
    private fun isCamelCase(declaration:Declaration): ValidationResult {
        return if(declaration is VariableDeclaration)
            isCamelCase(declaration.range, declaration.variableName)
        else
            ValidResult()
    }

    private fun isCamelCase(range: Range, variableName:String): ValidationResult {
        return if(variableName.matches(Regex("[a-z]+([A-Z][a-z]+)*"))) //h
            ValidResult()
        else
            WarningResult(range, "$variableName is not in Camel Case")
    }


}

