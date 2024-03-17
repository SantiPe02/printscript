package parser

import ast.Declaration
import ast.Range
import token.TokenInfo

sealed interface DeclarationValidator {
    fun declare(tokens: List<TokenInfo>, range: Range, i: Int): Declaration
}