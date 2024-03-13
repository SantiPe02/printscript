package lexer

import token.TokenInfo

interface Lexer {
    fun tokenize(code: String): List<TokenInfo>
}

class LexerImplementation: Lexer {

    override fun tokenize(code: String): List<TokenInfo> {
        val tokens = mutableListOf<TokenInfo>()
        var currentToken = ""
        //TODO: implement lexer logic.

        return tokens
    }
}