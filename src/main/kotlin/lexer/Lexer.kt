package lexer

import token.TokenInfo

interface Lexer {
    fun tokenize(input: String): List<TokenInfo>
}