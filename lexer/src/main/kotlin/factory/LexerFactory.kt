package factory

import lexer.Lexer

interface LexerFactory {
    fun create(): Lexer
}
