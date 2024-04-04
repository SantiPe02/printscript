import ast.*
import linterRules.LinterRule
import result.validation.WarningResult

/**
    'Until you make the unconscious conscious, it will direct your life
    and you will call it fate'

    Jung, C.G.
 */

sealed interface Linter {
    fun lintScope(scope: Scope, rules: List<LinterRule>) : List<WarningResult>
}

class MyLinter : Linter {

    override fun lintScope(scope: Scope, rules: List<LinterRule>) : List<WarningResult> {
        return lint(scope, scope, rules)
    }

    // need to modularize on future.
    /**
     * @param workingScope: It is the scope that is being analyzed.
     * @param ast: is the AST inside the working scope that is currently being analyzed. We will analyze them one by one.
     * @param rules: is the list of rules that are going to be applied to the AST.
     */
    private fun lint(workingScope: Scope, ast: AST, rules: List<LinterRule>): List<WarningResult>{
        val warningList = mutableListOf<WarningResult>()

        warningList += validateASTRules(workingScope, ast, rules);
        if(ast is Scope){
            for (element in ast.body){
                warningList += lint(workingScope, element, rules);
            }
        }

        else if(ast is Call){
            for (element in ast.arguments){
                warningList += lint(workingScope, element, rules);
            }
        }
        else if(ast is VariableDeclaration)
            warningList += lint(workingScope, ast.value, rules);

        else if(ast is AssignmentStatement)
            warningList += lint(workingScope, ast.value, rules);

        else if(ast is MethodResult)
            warningList += lint(workingScope, ast.methodCall, rules);

        else
            return warningList // If it is an argument, or a declaration statement, there is no need to go deeper

        return warningList
    }

    private fun validateASTRules(workingScope: Scope, ast: AST, rules: List<LinterRule>): List<WarningResult>{
        val warningList = mutableListOf<WarningResult>()
        for (rule in rules){
            val result = rule.ruleIsValid(workingScope, ast)
            if(result is WarningResult){
                warningList += result
            }
        }
        return warningList
    }

}
