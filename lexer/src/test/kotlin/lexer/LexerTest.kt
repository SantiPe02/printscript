package lexer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.TokenInfo
import token.TokenInfo.TokenType
import token.TokenInfo.Token
import token.TokenInfo.Position

class LexerTest {
    @Test
    fun lexerNumberTest(){
        val input = "let a: number = 3;"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "number"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "3"), Position(16, 16))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(17, 17))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerStringTest(){
        val input = "let a: string = \"hello\";"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "string"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "\"hello\""), Position(16, 22))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(23, 23))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerStringOfMultipleWordsTest(){
        val input = "let a: string = \"hello world\";"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "string"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "\"hello world\""), Position(16, 28))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(29, 29))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerSumOperatorTest(){
        val input = "let a: number = 3 + 2;"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "number"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "3"), Position(16, 16))
        val token7 = TokenInfo(Token(TokenType.OPERATOR, "+"), Position(18, 18))
        val token8 = TokenInfo(Token(TokenType.LITERAL, "2"), Position(20, 20))
        val token9 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(21, 21))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerDivideOperatorTest() {
        val input = "let a: number = 3 / 2;"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "number"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "3"), Position(16, 16))
        val token7 = TokenInfo(Token(TokenType.OPERATOR, "/"), Position(18, 18))
        val token8 = TokenInfo(Token(TokenType.LITERAL, "2"), Position(20, 20))
        val token9 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(21, 21))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerMultiplyOperatorTest() {
        val input = "let a: number = 3 * 2;"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "number"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "3"), Position(16, 16))
        val token7 = TokenInfo(Token(TokenType.OPERATOR, "*"), Position(18, 18))
        val token8 = TokenInfo(Token(TokenType.LITERAL, "2"), Position(20, 20))
        val token9 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(21, 21))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerSubtractOperatorTest() {
        val input = "let a: number = 3 - 2;"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "number"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "3"), Position(16, 16))
        val token7 = TokenInfo(Token(TokenType.OPERATOR, "-"), Position(18, 18))
        val token8 = TokenInfo(Token(TokenType.LITERAL, "2"), Position(20, 20))
        val token9 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(21, 21))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerBooleanTest(){
        val input = "let a: boolean = true;"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "boolean"), Position(7, 13))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(15, 15))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "true"), Position(17, 20))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(21, 21))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerIfElseTest() {
        val input = "if (a < b) { let c: string = \"hello\"; } else { let c: number = 2; }"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "if"), Position(0, 1))
        val token2 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "("), Position(3, 3))
        val token3 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token4 = TokenInfo(Token(TokenType.OPERATOR, "<"), Position(6, 6))
        val token5 = TokenInfo(Token(TokenType.IDENTIFIER, "b"), Position(8, 8))
        val token6 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ")"), Position(9, 9))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "{"), Position(11, 11))
        val token8 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(13, 15))
        val token9 = TokenInfo(Token(TokenType.IDENTIFIER, "c"), Position(17, 17))
        val token10 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(18, 18))
        val token11 = TokenInfo(Token(TokenType.KEYWORD, "string"), Position(20, 25))
        val token12 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(27, 27))
        val token13 = TokenInfo(Token(TokenType.LITERAL, "\"hello\""), Position(29, 35))
        val token14 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(36, 36))
        val token15 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "}"), Position(38, 38))
        val token16 = TokenInfo(Token(TokenType.KEYWORD, "else"), Position(40, 43))
        val token17 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "{"), Position(45, 45))
        val token18 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(47, 49))
        val token19 = TokenInfo(Token(TokenType.IDENTIFIER, "c"), Position(51, 51))
        val token20 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(52, 52))
        val token21 = TokenInfo(Token(TokenType.KEYWORD, "number"), Position(54, 59))
        val token22 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(61, 61))
        val token23 = TokenInfo(Token(TokenType.LITERAL, "2"), Position(63, 63))
        val token24 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(64, 64))
        val token25 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "}"), Position(66, 66))
        val expected = listOf(
            token1,
            token2,
            token3,
            token4,
            token5,
            token6,
            token7,
            token8,
            token9,
            token10,
            token11,
            token12,
            token13,
            token14,
            token15,
            token16,
            token17,
            token18,
            token19,
            token20,
            token21,
            token22,
            token23,
            token24,
            token25
        )
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerDeclaredVariableTest() {
        val input = "x = 5;";
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.IDENTIFIER, "x"), Position(0, 0))
        val token2 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(2, 2))
        val token3 = TokenInfo(Token(TokenType.LITERAL, "5"), Position(4, 4))
        val token4 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(5, 5))
        val expected = listOf(token1, token2, token3, token4)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerGreaterEqualTest(){
        val input = "if (a >= b) {return true;}"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "if"), Position(0, 1))
        val token2 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "("), Position(3, 3))
        val token3 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token4 = TokenInfo(Token(TokenType.OPERATOR, ">="), Position(6, 7))
        val token5 = TokenInfo(Token(TokenType.IDENTIFIER, "b"), Position(9, 9))
        val token6 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ")"), Position(10, 10))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "{"), Position(12, 12))
        val token8 = TokenInfo(Token(TokenType.KEYWORD, "return"), Position(13, 18))
        val token9 = TokenInfo(Token(TokenType.LITERAL, "true"), Position(20, 23))
        val token10 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(24, 24))
        val token11 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "}"), Position(25, 25))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9, token10, token11)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerLessEqualTest(){
        val input = "if (a <= b) {return true;}"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "if"), Position(0, 1))
        val token2 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "("), Position(3, 3))
        val token3 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token4 = TokenInfo(Token(TokenType.OPERATOR, "<="), Position(6, 7))
        val token5 = TokenInfo(Token(TokenType.IDENTIFIER, "b"), Position(9, 9))
        val token6 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ")"), Position(10, 10))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "{"), Position(12, 12))
        val token8 = TokenInfo(Token(TokenType.KEYWORD, "return"), Position(13, 18))
        val token9 = TokenInfo(Token(TokenType.LITERAL, "true"), Position(20, 23))
        val token10 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(24, 24))
        val token11 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "}"), Position(25, 25))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9, token10, token11)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerDifferentTest(){
        val input = "if (a != b) {return true;}"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "if"), Position(0, 1))
        val token2 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "("), Position(3, 3))
        val token3 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token4 = TokenInfo(Token(TokenType.OPERATOR, "!="), Position(6, 7))
        val token5 = TokenInfo(Token(TokenType.IDENTIFIER, "b"), Position(9, 9))
        val token6 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ")"), Position(10, 10))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "{"), Position(12, 12))
        val token8 = TokenInfo(Token(TokenType.KEYWORD, "return"), Position(13, 18))
        val token9 = TokenInfo(Token(TokenType.LITERAL, "true"), Position(20, 23))
        val token10 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(24, 24))
        val token11 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "}"), Position(25, 25))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9, token10, token11)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerEqualTest(){
        val input = "if (a == b) {return true;}"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "if"), Position(0, 1))
        val token2 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "("), Position(3, 3))
        val token3 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token4 = TokenInfo(Token(TokenType.OPERATOR, "=="), Position(6, 7))
        val token5 = TokenInfo(Token(TokenType.IDENTIFIER, "b"), Position(9, 9))
        val token6 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ")"), Position(10, 10))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "{"), Position(12, 12))
        val token8 = TokenInfo(Token(TokenType.KEYWORD, "return"), Position(13, 18))
        val token9 = TokenInfo(Token(TokenType.LITERAL, "true"), Position(20, 23))
        val token10 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(24, 24))
        val token11 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, "}"), Position(25, 25))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7, token8, token9, token10, token11)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerStringWithSpacesInBetweenTest(){
        val input = "let a: String = \"hello world\";";
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "String"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "\"hello world\""), Position(16, 28))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(29, 29))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerStringWithKeywordNameTest(){
        val input = "let a: String = \"String\";";
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "String"), Position(7, 12))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(14, 14))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "\"String\""), Position(16, 23))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(24, 24))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7)
        assertEquals(expected, lexer.tokenize(input));
    }

    @Test
    fun lexerFloatNumberTest() {
        val input = "let a: float = 3.14;"
        val lexer = LexerImpl()
        val token1 = TokenInfo(Token(TokenType.KEYWORD, "let"), Position(0, 2))
        val token2 = TokenInfo(Token(TokenType.IDENTIFIER, "a"), Position(4, 4))
        val token3 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ":"), Position(5, 5))
        val token4 = TokenInfo(Token(TokenType.KEYWORD, "float"), Position(7, 11))
        val token5 = TokenInfo(Token(TokenType.OPERATOR, "="), Position(13, 13))
        val token6 = TokenInfo(Token(TokenType.LITERAL, "3.14"), Position(15, 18))
        val token7 = TokenInfo(Token(TokenType.SPECIAL_SYMBOL, ";"), Position(19, 19))
        val expected = listOf(token1, token2, token3, token4, token5, token6, token7)
        assertEquals(expected, lexer.tokenize(input));
    }
}
