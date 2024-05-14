package parser.declarator

import ast.Declaration
import token.TokenInfo

sealed interface DeclarationValidator {
    fun declare(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration>
}
