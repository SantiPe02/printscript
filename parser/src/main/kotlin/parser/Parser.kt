package parser
import ast.AST
import ast.Scope
import token.TokenInfo
import token.TokenInfo.Token
import token.TokenInfo.TokenType

sealed interface Parser {
    fun parseTokens(tokenList: List<TokenInfo>): Result<Scope>
}

class MyParser : Parser {
    private val commons = ParserCommons()

    // Actually, should take, both a token list and a name (in here by default the name is "program")
    override fun parseTokens(tokenList: List<TokenInfo>): Result<Scope> {
        val astNodes = mutableListOf<AST>()
        var i = 0
        while (i < tokenList.size) {
            val tokenInfo = tokenList[i]
            val token = tokenInfo.token
            val astNode = ParserDeclarator().parseByTokenType(tokenList, tokenInfo, i)
            astNode
                .onFailure { return Result.failure(it) }
                .onSuccess { astNodes.add(it) }
            val length = lengthOfDeclaration(tokenList, token, i)
            length
                .onSuccess { i = it }
                .onFailure { return Result.failure(it) }
        }

        return Result.success(Scope("program", commons.getRangeOfTokenList(tokenList), astNodes))
    }

    /**
     * Length refers to the amount of tokens from the starting position.
     * e.g: let name:String = "Carlos Salvador"; --> Length = 7
     * */
    private fun lengthOfDeclaration(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): Result<Int> {
        return when (token.type) {
            TokenType.KEYWORD -> lengthOfKeywordDeclaration(tokens, token, i) // let, if, else, while, for, fun, return
            // a, name, password
            TokenType.IDENTIFIER -> commons.lengthTillFirstAppearanceNextOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
            else -> Result.success(1)
        }
    }

    private fun lengthOfKeywordDeclaration(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): Result<Int> {
        return when (token.text) {
            "let" -> commons.lengthTillFirstAppearanceNextOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
            "if" -> closingBracketsIndex(tokens, i)
            "const" -> commons.lengthTillFirstAppearanceNextOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
            else -> commons.lengthTillFirstAppearanceNextOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
        }
    }

    private fun closingBracketsIndex(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Int> {
        // es probable que esto acarree un error si hago, luego, dos o tres if seguidos.
        // Sí, es eso lo que pasa, quizá no esté tan claro, pero sí.
        val j = commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, "{", i)
        j.onSuccess { return conditionalClosingBracketsIndex(tokens, it) }
            .onFailure { return Result.failure(it) }

        return Result.failure(Exception("Unknown error " + commons.getRangeOfTokenAsString(tokens[i])))
    }

    private fun conditionalClosingBracketsIndex(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Int> {
        commons.searchForNextOfClosingCharacter(tokens, "{", i)
            .onSuccess {
                if (indexExists(tokens, it) && isElse(tokens, it)) {
                    return closingBracketsIndex(tokens, it + 1)
                } else {
                    return Result.success(it)
                }
            }
            .onFailure { return Result.failure(it) }
        return Result.failure(Exception("Unknown error " + commons.getRangeOfTokenAsString(tokens[i])))
    }

    private fun isElse(
        tokens: List<TokenInfo>,
        i: Int,
    ): Boolean {
        return tokens[i].token.text == "else"
    }

    private fun indexExists(
        tokens: List<TokenInfo>,
        i: Int,
    ): Boolean {
        return i < tokens.size
    }
}
