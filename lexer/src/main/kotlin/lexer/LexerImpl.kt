package lexer

import token.TokenInfo
import token.TokenInfo.Position
import token.TokenInfo.Token
import token.TokenInfo.TokenType
import util.KEY_WORD_PATTERN
import util.LITERAL_PATTERN
import util.OPERATOR_PATTERN
import util.SPECIAL_SYMBOL_PATTERN
import util.STRING_SEPARATOR_PATTERN

class LexerImpl : Lexer {
    private val regex = Regex(STRING_SEPARATOR_PATTERN)

    override fun tokenize(input: String): List<TokenInfo> {
        val tokens = mutableListOf<TokenInfo>()
        regex.findAll(input).forEach {
            val token = it.value.trim()
            if (token.isNotBlank()) {
                val type =
                    when {
                        token.matches(Regex(KEY_WORD_PATTERN, RegexOption.IGNORE_CASE)) -> TokenType.KEYWORD
                        token.matches(Regex(LITERAL_PATTERN)) -> TokenType.LITERAL
                        token.matches(Regex(OPERATOR_PATTERN, RegexOption.IGNORE_CASE)) -> TokenType.OPERATOR
                        token.matches(Regex(SPECIAL_SYMBOL_PATTERN, RegexOption.IGNORE_CASE)) -> TokenType.SPECIAL_SYMBOL
                        else -> TokenType.IDENTIFIER
                    }
                tokens.add(TokenInfo(Token(type, token), Position(it.range.first, it.range.last)))
            }
        }
        return tokens
    }
}
