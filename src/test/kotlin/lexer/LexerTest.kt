package lexer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import token.TokenInfo

class LexerTest {

    @Test
    fun testTokenize() {
        val lexer = LexerImplementation()
        val tokens: List<TokenInfo> = mutableListOf<TokenInfo>()

        assertEquals(tokens, lexer.tokenize(""))
    }
}