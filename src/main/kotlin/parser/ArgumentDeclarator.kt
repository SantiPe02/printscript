package parser

import ast.Argument
import ast.Range
import token.TokenInfo

sealed interface ArgumentDeclarator {
    fun declareArgument(range: Range, tokens: List<TokenInfo>, arguments: List<TokenInfo>, i: Int): Argument
}