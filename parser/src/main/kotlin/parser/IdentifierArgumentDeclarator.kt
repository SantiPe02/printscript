package parser

import ast.Argument
import ast.Range
import ast.VariableArgument
import token.TokenInfo

class IdentifierArgumentDeclarator: ArgumentDeclarator {
    // It should recieve a list of only one element
    val commons = ParserCommons();
    override fun declareArgument(tokens: List<TokenInfo>, arguments: List<TokenInfo>, i: Int): Argument {
        return VariableArgument(commons.getRangeOfTokenList(arguments), arguments[0].token.text)
    }
}