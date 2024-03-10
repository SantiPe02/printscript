package token

/**
 * Token info poses the Token which is composed by the TokenType and text for each specific case of that token.
 * And a position to know from witch part of the file that token is related too.
 * To find more information about the TokenType go to the documentation.
 */
class TokenInfo(val token: Token, val position: Position) {
    data class Position(val filePos: Long, val lenght: Int)
    data class Token(val type: TokenType, val text: String)
    enum class TokenType {
        KEYWORD,
        SPECIAL_SYMBOL,
        OPERATOR,
        IDENTIFIER,
        LITERAL
    }
}

