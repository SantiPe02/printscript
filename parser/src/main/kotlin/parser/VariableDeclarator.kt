package parser

import ast.Argument
import ast.AssignmentStatement
import ast.Declaration
import ast.DeclarationStatement
import ast.VariableDeclaration
import token.TokenInfo

class VariableDeclarator : DeclarationValidator {
    val commons: ParserCommons = ParserCommons()

    /* todo:
        --> ¿What if I have let num:int > 6?
        --> Brackets should close.
        --> There is a lot of circular dependency. Maybe should put all the "ParserCommons()" inside parser.
     */

    // 3 casos:
    // 1. let name:String = "Carlos Salvador";
    // 2. let name:String;
    // 3. name = "Carlos Salvador";
    // y luego también puede pasar que: name > < != <= >= number (son methods, creo)
    override fun declare(
        tokens: List<TokenInfo>,
        i: Int,
    ): Declaration {
        var j = i
        val variableName = commons.getTokenByType(tokens[++j].token, TokenInfo.TokenType.IDENTIFIER)
        val variable = tokens[j]

        ++j
        if (commons.isOfTextAndType(tokens[j].token, ":", TokenInfo.TokenType.SPECIAL_SYMBOL)) {
            return handleVariableDeclaration(tokens, j, variable, variableName)
        } else if (commons.isOfTextAndType(tokens[j].token, "=", TokenInfo.TokenType.OPERATOR)) {
            return AssignmentStatement(
                commons.getRangeOfTokenList(listOf(variable)),
                variableName.text,
                getVariableArgument(tokens, ++j),
            )
        } else {
            throw Exception("Invalid variable declaration")
        }
    }

    private fun handleVariableDeclaration(
        tokens: List<TokenInfo>,
        i: Int,
        variable: TokenInfo,
        variableName: TokenInfo.Token,
    ): Declaration {
        var j = i
        val variableType = commons.getTokenByType(tokens[++j].token, TokenInfo.TokenType.KEYWORD)

        ++j
        return if (commons.isOfTextAndType(tokens[j].token, "=", TokenInfo.TokenType.OPERATOR)) {
            fullVariableDeclaration(tokens, j, variable, variableName, variableType)
        } else if (commons.isEndOfVarChar(tokens, j)) {
            DeclarationStatement(commons.getRangeOfTokenList(listOf(variable)), variableName.text, variableType.text)
        } else {
            throw Exception("Invalid variable declaration")
        }
    }

    private fun fullVariableDeclaration(
        tokens: List<TokenInfo>,
        i: Int,
        variable: TokenInfo,
        variableName: TokenInfo.Token,
        variableType: TokenInfo.Token,
    ): Declaration {
        var j = i
        val variableArgument: Argument = getVariableArgument(tokens, ++j)
        return VariableDeclaration(commons.getRangeOfTokenList(listOf(variable)), variableName.text, variableType.text, variableArgument)
    }

    private fun getVariableArgument(
        tokens: List<TokenInfo>,
        i: Int,
    ): Argument {
        val arguments = getVariableArguments(tokens, i)

        return VariableArgumentDeclarator().declareArgument(tokens, arguments, i)
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
