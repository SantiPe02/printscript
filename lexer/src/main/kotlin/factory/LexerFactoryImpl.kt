package factory

import lexer.Lexer
import lexer.LexerImpl
import util.KEY_WORD_PATTERN_V10
import util.KEY_WORD_PATTERN_V11
import util.LITERAL_PATTERN
import util.OPERATOR_PATTERN
import util.SPECIAL_SYMBOL_PATTERN

class LexerFactoryImpl(val version: String) : LexerFactory {
    override fun create(): Lexer {
        return when (version) {
            "1.0" -> LexerImpl(KEY_WORD_PATTERN_V10, LITERAL_PATTERN, OPERATOR_PATTERN, SPECIAL_SYMBOL_PATTERN)
            "1.1" -> LexerImpl(KEY_WORD_PATTERN_V11, LITERAL_PATTERN, OPERATOR_PATTERN, SPECIAL_SYMBOL_PATTERN)
            else -> throw IllegalArgumentException("Unknown version")
        }
    }
}
