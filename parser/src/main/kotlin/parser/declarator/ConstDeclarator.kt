package parser.declarator

import ast.Argument
import ast.ConstantDeclaration
import ast.Declaration
import parser.ParserCommons
import token.TokenInfo

class ConstDeclarator {
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
        } else {
            return Result.failure(Exception("Expected \":\" at " + commons.getRangeOfTokenAsString(tokens[j])))
        }
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
                return declareConst(tokens, j, variable, variableName, variableType)
            } else {
                return Result.failure(Exception("Expected \"=\" at " + commons.getRangeOfTokenAsString(tokens[j])))
            }
        }
        return Result.failure(Exception("Invalid variable declaration"))
    }

    fun declareConst(
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
                ConstantDeclaration(commons.getRangeOfTokenList(listOf(variable)), variableName.text, variableType.text, it),
            )
        }.onFailure { return Result.failure(it) }

        return Result.failure(Exception("Invalid variable declaration"))
    }
}
