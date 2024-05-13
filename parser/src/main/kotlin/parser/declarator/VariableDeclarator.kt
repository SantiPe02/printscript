package parser.declarator

import ast.Declaration
import parser.ParserCommons
import token.TokenInfo
import token.TokenInfo.TokenType as type

class VariableDeclarator : DeclarationValidator {
    val commons: ParserCommons = ParserCommons()

    override fun declare(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration> {
        return handleIdentifierAndKeyword(tokens, i)
    }

    private fun handleIdentifierAndKeyword(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration> {
        commons.getTokenIfIsOfType(tokens[i].token, type.IDENTIFIER).onSuccess {
            return LetDeclarator().handleVariableOnNameSuccess(tokens, i, it)
        }
        return handleKeyword(tokens, i)
    }

    private fun handleKeyword(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration> {
        val varName = commons.getTokenIfIsOfType(tokens[i].token, type.KEYWORD) // No es el var name, es el type.
        varName.onFailure { return Result.failure(it) }
        varName.onSuccess {
            return declareByVarType(it, tokens, i)
        }
        return Result.failure(Exception("Invalid variable declaration"))
    }

    private fun declareByVarType(
        name: TokenInfo.Token,
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration> {
        var j = i
        val next = commons.getTokenIfIsOfType(tokens[++j].token, type.IDENTIFIER)
        next.onSuccess {
            return when (name.text) {
                "let" -> LetDeclarator().handleVariableOnNameSuccess(tokens, j, it)
                "const" -> ConstDeclarator().handleVariableOnNameSuccess(tokens, j, it)
                else -> Result.failure(Exception("Invalid variable type"))
            }
        }.onFailure { return Result.failure(it) }
        return Result.failure(Exception("Invalid variable declaration"))
    }
}
