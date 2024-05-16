package linterRules

import ast.AST
import ast.Call
import ast.MethodResult
import result.validation.ValidResult
import result.validation.ValidationResult
import result.validation.WarningResult

class FunctionCallWithoutExpressionRule {
    fun validate(
        tree: AST,
        funcName: String,
    ): ValidationResult {
        // la lista del call .size == 1 && !MethodDeclaration.
        return if (tree is Call) {
            if (tree.name != funcName) {
                println("Acá")
                println(tree.name)
                println(funcName)
                ValidResult()
            } else {
                if (tree.arguments.size == 1 && !(tree.arguments.first() is MethodResult)) {
                    println("Acá 1")
                    ValidResult()
                } else if (tree.arguments.isEmpty()) {
                    println("Acá 2")
                    ValidResult()
                } else {
                    WarningResult(tree.range, "$funcName should not have an expression inside it.")
                }
            }
        } else {
            println("Acá 3")
            ValidResult()
        }
    }
}
