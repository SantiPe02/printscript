import ast.*
import com.google.gson.Gson
import configurationReader.JsonConfigurationReader
import linterRules.LinterRule
import result.validation.WarningResult
import java.io.File

/**
    'Until you make the unconscious conscious, it will direct your life
    and you will call it fate'

    Jung, C.G.
 */

sealed interface Linter {
    fun lint(ast: AST, rules: List<LinterRule>) : List<WarningResult>
}

class MyLinter : Linter {

    // need to modularize on future.
    override fun lint(ast: AST, rules: List<LinterRule>): List<WarningResult>{
        val warningList = mutableListOf<WarningResult>()

        warningList += validateASTRules(ast, rules);
        if(ast is Scope){
            for (element in ast.body){
                warningList += lint(element, rules);
            }
        }
        else if(ast is Call){
            for (element in ast.arguments){
                warningList += lint(element, rules);
            }
        }
        else if(ast is VariableDeclaration)
            warningList += lint(ast.value, rules);

        else if(ast is AssignmentStatement)
            warningList += lint(ast.value, rules);

        else if(ast is MethodResult)
            warningList += lint(ast.methodCall, rules);

        else
            return warningList // If it is an argument, or a declaration statement, there is no need to go deeper

        return warningList
    }

    private fun validateASTRules(ast: AST, rules: List<LinterRule>): List<WarningResult>{
        val warningList = mutableListOf<WarningResult>()
        for (rule in rules){
            val result = rule.ruleIsValid(ast)
            if(result is WarningResult){
                warningList += result
            }
        }
        return warningList
    }

}




// básicamente, traer todas las reglas del configReader, luego, por cada Scope, recursivamente llamar a todos los AST internos
// y aplicar las reglas para cada uno.

// por cada elemento de la lista del scope aplicar reglas
// si el elemento internos no es un literal o variable argument o un "statement", entonces seguir hasta llegar a alguno de los casos
// obvio q el MEthodResult tiene Call y el variableDeclaration "value" y el Call arguments y lo que quieras
// para cada caso especifico resolvete.
// finalmente, devolver una lista con todos los warnings q fuiste reciviendo
// para testear, assertEquals(List(WarningResult(x), WarningResult(y)...), MyLinter().lint(ASTFile))


