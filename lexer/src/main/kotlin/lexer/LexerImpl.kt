package lexer

import token.TokenInfo
import token.TokenInfo.Token
import token.TokenInfo.TokenType
import token.TokenInfo.Position
import util.*

class LexerImpl: Lexer {
    private val regex = Regex(stringSeparatorPattern);
    override fun tokenize(input: String): List<TokenInfo> {
        val tokens = mutableListOf<TokenInfo>();
        regex.findAll(input).forEach {
            val token = it.value.trim()
            if (token.isNotBlank()){
                val type = when {
                    token.matches(Regex(keywordPattern, RegexOption.IGNORE_CASE)) -> TokenType.KEYWORD
                    token.matches(Regex(literalPattern)) -> TokenType.LITERAL
                    token.matches(Regex(operatorPattern, RegexOption.IGNORE_CASE)) -> TokenType.OPERATOR
                    token.matches(Regex(specialSymbolPattern, RegexOption.IGNORE_CASE)) -> TokenType.SPECIAL_SYMBOL
                    else -> TokenType.IDENTIFIER
                }
                tokens.add(TokenInfo(Token(type, token), Position(it.range.first, it.range.last)))
            }
        }
        return tokens
    }
}
