package parser

import ast.LiteralArgument
import ast.Range
import token.TokenInfo

class LiteralArgumentDeclarator : ArgumentDeclarator {
    // It should recieve a list of only one element
    val commons = ParserCommons();
    override fun declareArgument(tokens: List<TokenInfo>, arguments: List<TokenInfo>, i: Int): LiteralArgument {
        return LiteralArgument(commons.getRangeOfTokenList(arguments), arguments[0].token.text, commons.getDataTypeFromString(arguments[0].token.text))
    }
}