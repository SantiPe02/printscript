package parser

import ast.*
import token.TokenInfo

class VariableDeclarator : DeclarationValidator {
    val commons:ParserCommons = ParserCommons();

    /* todo:
        --> ¿What if I have let num:int > 6?
        --> Brackets should close.
        --> There is a lot of circular dependency. Maybe should put all the "ParserCommons()" inside parser.
    */

    override fun declare(tokens: List<TokenInfo>,  i: Int): Declaration {
        var j = i
        val variableName = commons.getTokenByType(tokens[++j].token, TokenInfo.TokenType.IDENTIFIER)
        val variable = tokens[j]
        commons.getTokenByTextAndType(tokens[++j].token, ":", TokenInfo.TokenType.SPECIAL_SYMBOL)
        val variableType = commons.getTokenByType(tokens[++j].token, TokenInfo.TokenType.KEYWORD)
        commons.getTokenByTextAndType(tokens[++j].token, "=", TokenInfo.TokenType.OPERATOR)

        val arguments = getVariableArguments(tokens, ++j) // arguments could be empty, which is the same to saying let name:String;
        val variableArgument: Argument = VariableArgumentDeclarator().declareArgument(tokens, arguments, j)


        return VariableDeclaration(commons.getRangeOfTokenList(listOf(variable)), variableName.text, variableType.text, variableArgument)
    }


    fun getVariableArguments(tokens: List<TokenInfo>, i: Int): List<TokenInfo> {
        val arguments = mutableListOf<TokenInfo>()
        var j = i
        while(j < tokens.size) // si hay una suma o algo raro repetir los métodos y así...
        {
            if(commons.isEndOfVarChar(tokens, j)){
                break
            }
            else{
                arguments.add(tokens[j])}
            j++
        }
        return arguments
    }

}