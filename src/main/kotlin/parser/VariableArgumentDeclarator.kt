package parser

import ast.Argument
import ast.Range
import token.TokenInfo

class VariableArgumentDeclarator {
    // It should recieve a list of only one element
    val commons = ParserCommons();
    fun declareArgument(range: Range, tokens: List<TokenInfo>, arguments: List<TokenInfo>, i: Int): Argument {
        return when(arguments.size){
            1 -> argumentDeclarationOfSize1(range, arguments[0])
            else -> MethodResultDeclarator().declareArgument(range, tokens, arguments, i)
        }
    }

    fun argumentDeclarationOfSize1(range: Range, argument: TokenInfo): Argument {
        // methods, on smaller cases, have three arguments: methodName, "(" and ")": they don't fit here.
        return when(argument.token.type){
            TokenInfo.TokenType.LITERAL  -> LiteralArgumentDeclarator().declareArgument(range, listOf(argument), listOf(argument), 0)
            TokenInfo.TokenType.IDENTIFIER -> IdentifierArgumentDeclarator().declareArgument(range, listOf(argument), listOf(argument), 0)
            else -> throw Exception("Invalid sintax: there should be a single argument after the equal operator \" co\"")
        }
    }
}