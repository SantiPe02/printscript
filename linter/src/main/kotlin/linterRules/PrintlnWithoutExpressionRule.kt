package linterRules

import ast.AST
import ast.Call
import ast.MethodResult
import ast.Scope
import result.validation.ValidResult
import result.validation.ValidationResult
import result.validation.WarningResult

class PrintlnWithoutExpressionRule : LinterRule {
    override fun ruleIsValid(scope: Scope, tree: AST): ValidationResult {
        // la lista del call .size == 1 && !MethodDeclaration.
        return if(tree is Call){
            if(tree.name != "println")
                ValidResult()
            else {
                if(tree.arguments.size == 1 && !(tree.arguments.first() is MethodResult))
                    ValidResult()
                else if(tree.arguments.isEmpty())
                    ValidResult()
                else
                    WarningResult(tree.range, "println should not have an expression inside it.")
            }
        } else
            ValidResult()

    }
}