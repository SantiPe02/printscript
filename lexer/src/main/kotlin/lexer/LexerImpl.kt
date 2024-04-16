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

class LexerImpl() : Lexer {
    private val regex = Regex(STRING_SEPARATOR_PATTERN)

    override fun tokenize(
        input: String,
        context: Context,
    ): List<TokenInfo> {
        val tokens = mutableListOf<TokenInfo>()
        regex.findAll(input).forEach {
            var token = it.value.trim()
            if (token.isNotBlank()) {
                val type =
                    when {
                        token.matches(Regex(KEY_WORD_PATTERN, RegexOption.IGNORE_CASE)) -> TokenType.KEYWORD
                        token.matches(Regex(LITERAL_PATTERN)) -> {
                            if (token.matches(Regex("""("[^"]*")"""))) {
                                token = token.substring(1, token.length - 1)
                            }
                            TokenType.LITERAL
                        }
                        token.matches(Regex(OPERATOR_PATTERN, RegexOption.IGNORE_CASE)) -> TokenType.OPERATOR
                        token.matches(Regex(SPECIAL_SYMBOL_PATTERN, RegexOption.IGNORE_CASE)) -> TokenType.SPECIAL_SYMBOL
                        else -> TokenType.IDENTIFIER
                    }
                tokens.add(
                    TokenInfo(
                        Token(type, token),
                        Position(context.startingLength + it.range.first, context.startingLength + it.range.last),
                    ),
                )
            }
        }
        return tokens
    }
}
