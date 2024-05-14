package parser.declarator

import ast.Argument
import parser.ParserCommons
import token.TokenInfo

class VariableArgumentDeclarator {
    // It should recieve a list of only one element
    val commons = ParserCommons()

    fun declareArgument(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Argument> {
        val arguments: List<TokenInfo> = getVariableArguments(tokens, i)
        return when (arguments.size) {
            1 -> argumentDeclarationOfSize1(arguments[0])
            0 ->
                Result.failure(
                    Exception(
                        "Invalid syntax: there should be a at least one argument after the equal operator " +
                            commons.getRangeOfTokenAsString(
                                tokens[i],
                            ),
                    ),
                )
            else -> MethodResultDeclarator().declareArgument(tokens, arguments, i)
        }
    }

    fun declareArgument(
        tokens: List<TokenInfo>,
        arguments: List<TokenInfo>,
        i: Int,
    ): Result<Argument> {
        return when (arguments.size) {
            1 -> argumentDeclarationOfSize1(arguments[0])
            else -> MethodResultDeclarator().declareArgument(tokens, arguments, i)
        }
    }

    fun argumentDeclarationOfSize1(argument: TokenInfo): Result<Argument> {
        // methods, on smaller cases, have three arguments: methodName, "(" and ")": they don't fit here.
        return when (argument.token.type) {
            TokenInfo.TokenType.LITERAL -> LiteralArgumentDeclarator().declareArgument(listOf(argument), listOf(argument), 0)
            TokenInfo.TokenType.IDENTIFIER -> IdentifierArgumentDeclarator().declareArgument(listOf(argument), listOf(argument), 0)
            else -> Result.failure(Exception("Invalid sintax: there should be a single argument after the equal operator \" co\""))
        }
    }

    private fun getVariableArguments(
        tokens: List<TokenInfo>,
        i: Int,
    ): List<TokenInfo> {
        val arguments = mutableListOf<TokenInfo>()
        var j = i
        while (j < tokens.size) // si hay una suma o algo raro repetir los métodos y así...
        {
            if (commons.isEndOfVarChar(tokens, j)) {
                break
            } else {
                arguments.add(tokens[j])
            }
            j++
        }
        return arguments
    }
}
