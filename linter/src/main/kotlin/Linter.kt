import ast.AST

/**
    'Until you make the unconscious conscious, it will direct your life
    and you will call it fate'

    Jung, C.G.
 */

sealed interface Linter {
    fun lint(ASTFile: AST)
}

// For each DECLARATION, we are going to lint.
// if there is a declaration inside a declaration, say
//      fun{ var...}
// then it recursively calls the lint function for the inner declaration.

// AST tendrá un método que devuelva TODAS las declaraciones (?)
class MyLinter : Linter {
    override fun lint(ASTFile: AST) {
    // básicamente, traer todas las reglas del configReader, luego, por cada Scope, recursivamente llamar a todos los AST internos
    // y aplicar las reglas para cada uno.

        // por cada elemento de la lista del scope aplicar reglas
        // si el elemento internos no es un literal o variable argument o un "statement", entonces seguir hasta llegar a alguno de los casos
        // obvio q el MEthodResult tiene Call y el variableDeclaration "value" y el Call arguments y lo que quieras
        // para cada caso especifico resolvete.
        // finalmente, devolver una lista con todos los warnings q fuiste reciviendo
        // para testear, assertEquals(List(WarningResult(x), WarningResult(y)...), MyLinter().lint(ASTFile))


    }

}


