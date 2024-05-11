package lexer

import token.TokenInfo

interface Lexer {
    fun tokenize(
        input: String,
        context: Context = Context(0),
    ): List<TokenInfo>
}

public class Context(val startingLength: Int)
