package token

/**
 * Token info poses the Token which is composed by the TokenType and text for each specific case of that token.
 * And a position to know from witch part of the file that token is related too.
 * To find more information about the TokenType go to the documentation.
 */
class TokenInfo(val token: Token, val position: Position) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TokenInfo) return false
        if (token != other.token) return false
        if (position != other.position) return false
        return true
    }

    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }
    data class Position(val startIndex: Int, val endIndex: Int)
    data class Token(val type: TokenType, val text: String)
    enum class TokenType {
        KEYWORD,
        SPECIAL_SYMBOL,
        OPERATOR,
        IDENTIFIER,
        LITERAL
    }
}

