package parser

import ast.Argument
import ast.AssignmentStatement
import ast.Declaration
import ast.DeclarationStatement
import ast.VariableDeclaration
import token.TokenInfo

class VariableDeclarator : DeclarationValidator {
    val commons: ParserCommons = ParserCommons()

    /* todo:
        --> ¿What if I have let num:int > 6?
        --> Brackets should close.
        --> There is a lot of circular dependency. Maybe should put all the "ParserCommons()" inside parser.
     */

    // RECORDÁ, TIENE UN ARGUMENT: ESE ARGUMENT NO ES RESULT. A MENOS Q SEA ERROR, EN CUYO CASO SE ELEVA.

    // 3 casos:
    // 1. let name:String = "Carlos Salvador";
    // 2. let name:String;
    // 3. name = "Carlos Salvador";
    // y luego también puede pasar que: name > < != <= >= number (son methods, creo)
    override fun declare(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration> {
        var j = i
        val varName = commons.getTokenByType(tokens[++j].token, TokenInfo.TokenType.IDENTIFIER)
        varName.onFailure { return Result.failure(it) }
        varName.onSuccess {
            return handleVariableNameSuccess(tokens, j, it)
        }
        return Result.failure(Exception("Invalid variable declaration, 1"))
    }

    private fun handleVariableNameSuccess(
        tokens: List<TokenInfo>,
        i: Int,
        variableName: TokenInfo.Token,
    ): Result<Declaration> {
        var j = i
        val variable = tokens[j]
        ++j

        if (commons.isOfTextAndType(tokens[j].token, ":", TokenInfo.TokenType.SPECIAL_SYMBOL)) {
            return handleVariableDeclaration(tokens, j, variable, variableName)
        } else if (commons.isOfTextAndType(tokens[j].token, "=", TokenInfo.TokenType.OPERATOR)) {
            val args = getVariableArgument(tokens, ++j)
            args.onSuccess {
                return Result.success(
                    AssignmentStatement(
                        commons.getRangeOfTokenList(listOf(variable)),
                        variableName.text,
                        it,
                    ),
                )
            }
            args.onFailure { return Result.failure(it) }
        }
        return Result.failure(Exception("Invalid variable declaration"))
    }

    private fun handleVariableDeclaration(
        tokens: List<TokenInfo>,
        i: Int,
        variable: TokenInfo,
        variableName: TokenInfo.Token,
    ): Result<Declaration> {
        var j = i
        val varType = commons.getTokenByType(tokens[++j].token, TokenInfo.TokenType.KEYWORD)
        varType.onFailure { return Result.failure(it) }
        varType.onSuccess {
            val variableType = it

            ++j
            if (commons.isOfTextAndType(tokens[j].token, "=", TokenInfo.TokenType.OPERATOR)) {
                return fullVariableDeclaration(tokens, j, variable, variableName, variableType)
            } else if (commons.isEndOfVarChar(tokens, j)) {
                val declaration =
                    DeclarationStatement(
                        commons.getRangeOfTokenList(listOf(variable)),
                        variableName.text,
                        variableType.text,
                    )
                return Result.success(declaration)
            }
        }
        return Result.failure(Exception("Invalid variable declaration, 2"))
    }

    private fun fullVariableDeclaration(
        tokens: List<TokenInfo>,
        i: Int,
        variable: TokenInfo,
        variableName: TokenInfo.Token,
        variableType: TokenInfo.Token,
    ): Result<Declaration> {
        var j = i
        val variableArgument: Result<Argument> = getVariableArgument(tokens, ++j)
        variableArgument.onSuccess {
            return Result.success(
                VariableDeclaration(commons.getRangeOfTokenList(listOf(variable)), variableName.text, variableType.text, it),
            )
        }

        return Result.failure(Exception("Invalid variable declaration, 3"))
    }

    private fun getVariableArgument(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Argument> {
        val arguments: List<TokenInfo> = getVariableArguments(tokens, i)
        val declaration = VariableArgumentDeclarator().declareArgument(tokens, arguments, i)
        declaration.onFailure { return Result.failure(it) }

        return declaration
    }

    private fun getVariableArguments(
        tokens: List<TokenInfo>,
        i: Int,
    ): List<TokenInfo> {
        val arguments = mutableListOf<TokenInfo>()
        var j = i
        while (j < tokens.size) // si hay una suma o algo raro repetir los métodos y así...
        {
            if (commons.isEndOfVarChar(tokens, j)) {
                break
            } else {
                arguments.add(tokens[j])
            }
            j++
        }
        return arguments
    }
}
