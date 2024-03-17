package parser

import ast.Argument
import ast.Range
import ast.VariableArgument
import token.TokenInfo

class IdentifierArgumentDeclarator: ArgumentDeclarator {
    // It should recieve a list of only one element
    override fun declareArgument(range: Range, tokens: List<TokenInfo>, arguments: List<TokenInfo>, i: Int): Argument {
        return VariableArgument(range, arguments[0].token.text)
    }
}