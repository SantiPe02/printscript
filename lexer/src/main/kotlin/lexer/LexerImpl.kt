package lexer

import token.TokenInfo
import token.TokenInfo.Position
import token.TokenInfo.Token
import token.TokenInfo.TokenType
import util.STRING_SEPARATOR_PATTERN

class LexerImpl(
    val keywordPattern: String,
    val literalPattern: String,
    val operatorPattern: String,
    val specialSymbolPattern: String,
) : Lexer {
    private val regex = Regex(STRING_SEPARATOR_PATTERN) // Separa el string

    override fun tokenize(
        input: String,
        context: Context, // tiene el index de donde estés parado. Como un puntero
    ): List<TokenInfo> {
        val tokens = mutableListOf<TokenInfo>() // lista q devolverá

        // para cada uno de los string q separaste en regex.
        regex.findAll(input).forEach {
            var token = it.value.trim()
            if (token.isNotBlank()) { // si no es espacio.

                // dependiendo con q matchea le da un Type diferente. Si no es ninguno es LITERAL (como "boca la concha de tu madre")
                val type =
                    when {
                        token.matches(Regex(keywordPattern, RegexOption.IGNORE_CASE)) -> TokenType.KEYWORD
                        token.matches(Regex(literalPattern)) -> {
                            if (token.matches(Regex("""("[^"]*")"""))) {
                                token = token.substring(1, token.length - 1)
                            }
                            TokenType.LITERAL
                        }
                        token.matches(Regex(operatorPattern, RegexOption.IGNORE_CASE)) -> TokenType.OPERATOR
                        token.matches(Regex(specialSymbolPattern, RegexOption.IGNORE_CASE)) -> TokenType.SPECIAL_SYMBOL
                        else -> TokenType.IDENTIFIER
                    }

                // crea el token info.
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
