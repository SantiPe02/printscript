package parser.declarator

import ast.Argument
import token.TokenInfo

sealed interface ArgumentDeclarator {
    fun declareArgument(
        tokens: List<TokenInfo>,
        arguments: List<TokenInfo>,
        i: Int,
    ): Result<Argument>
}
