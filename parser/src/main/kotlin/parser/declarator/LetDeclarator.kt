package parser.declarator

import ast.Argument
import ast.AssignmentStatement
import ast.Declaration
import ast.DeclarationStatement
import ast.VariableDeclaration
import parser.ParserCommons
import token.TokenInfo

class LetDeclarator {
    val commons: ParserCommons = ParserCommons()

    fun handleVariableOnNameSuccess(
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
            return declareAssignementStatement(tokens, j, variable, variableName)
        }

        return Result.failure(Exception("Invalid variable declaration " + commons.getRangeOfTokenAsString(tokens[i])))
    }

    fun declareAssignementStatement(
        tokens: List<TokenInfo>,
        i: Int,
        variable: TokenInfo,
        variableName: TokenInfo.Token,
    ): Result<Declaration> {
        var j = i
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
        return Result.failure(Exception("Invalid variable declaration"))
    }

    private fun handleVariableDeclaration(
        tokens: List<TokenInfo>,
        i: Int,
        variable: TokenInfo,
        variableName: TokenInfo.Token,
    ): Result<Declaration> {
        var j = i
        val varType = commons.getTokenIfIsOfType(tokens[++j].token, TokenInfo.TokenType.KEYWORD)
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
        val variableArgument: Result<Argument> = VariableArgumentDeclarator().declareArgument(tokens, ++j)
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
        val declaration = VariableArgumentDeclarator().declareArgument(tokens, i)
        declaration.onFailure { return Result.failure(it) }
        return declaration
    }
}
