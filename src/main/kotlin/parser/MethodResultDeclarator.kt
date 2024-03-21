package parser

import ast.Argument
import ast.Call
import ast.MethodResult
import ast.Range
import token.TokenInfo

class MethodResultDeclarator : ArgumentDeclarator {
    val commons = ParserCommons();

    override fun declareArgument(tokens: List<TokenInfo>, arguments: List<TokenInfo>, i: Int): MethodResult{
        val endIndex = commons.getEndOfVarIndex(tokens, i)
        val argument = methodArgument( tokens, i, endIndex, arguments)

        return argument
    }

    fun methodArgument(tokens: List<TokenInfo>, i: Int, endIndex: Int, arguments: List<TokenInfo>): MethodResult {


        val methodOperator: Int = commons.searchForFirstOperator(arguments, 0, arguments.size)
        val args: List<List<TokenInfo>> = createMethodByOperator(tokens, i, endIndex, i + methodOperator)
        val operator: TokenInfo = getOperatorMethod(tokens, i, i + methodOperator)


        val orderedArgs = sortTokenInfoListByPosition(args.flatten())
        val finalArguments = getFinalArgumentsOfMethodResult(args,arguments)

        return MethodResult(commons.getRangeOfTokenList(listOf(operator)), Call(commons.getRangeOfTokenList(orderedArgs), operator.token.text, finalArguments))
    }

    fun sortTokenInfoListByPosition(tokens: List<TokenInfo>): List<TokenInfo>{
        return tokens.sortedBy { it.position.startIndex}
    }
    fun createMethodByOperator(tokens: List<TokenInfo>, i: Int, endIndex: Int, methodOperator: Int): List<List<TokenInfo>> {
        return when(tokens[methodOperator].token.text){
            "+", "-", "*", "/" -> separateArguments( tokens, i, endIndex, methodOperator)
            "(" -> getParenthesesArguments(tokens, methodOperator)
            else -> throw Exception("Invalid operator")
        }
    }

    fun separateArguments(tokens: List<TokenInfo>, i: Int, endIndex: Int, operatorIndex: Int): List<List<TokenInfo>> {
        val leftArgs = tokens.subList(i, operatorIndex)
        val rightArgs = tokens.subList(operatorIndex + 1, endIndex)
        return listOf(leftArgs, rightArgs)
    }

    fun getParenthesesArguments( tokens: List<TokenInfo>, methodOperator: Int): List<List<TokenInfo>> {
        val closingParentheses = commons.searchForClosingCharacter(tokens, "(", methodOperator)
        var args = listOf(tokens.subList(methodOperator+1, closingParentheses))
        return listOf(tokens.subList(methodOperator+1, closingParentheses))
    }

    fun getOperatorMethod(tokens: List<TokenInfo>, i: Int, methodOperator: Int): TokenInfo{
        return when(tokens[methodOperator].token.text){
            "+", "-", "*", "/" -> tokens[methodOperator]
            "(" -> getOperatorWhenParentheses(tokens, i, methodOperator)
            else -> throw Exception("Todo")
        }
    }

    fun getOperatorWhenParentheses( tokens: List<TokenInfo>, i: Int, methodOperator: Int): TokenInfo{
        // two options, either it is test(args) or (args)
        if(methodOperator == 0)
            return tokens[methodOperator]
        return when(tokens[methodOperator-1].token.type){
            TokenInfo.TokenType.IDENTIFIER -> {tokens[methodOperator-1]}
            else -> tokens[methodOperator]
        }
    }

    fun getFinalArgumentsOfMethodResult(args: List<List<TokenInfo>>,arguments: List<TokenInfo>): List<Argument> {
        val finalArguments: MutableList<Argument> = mutableListOf()
        for (arg in args) {
            if(arg.isEmpty())
                throw Exception("Invalid sintax: there are no arguments on list")
            else if (arg.size != 1){
                finalArguments.add(methodArgument( arg, 0, arg.size , arg))}
            else
                finalArguments.add(VariableArgumentDeclarator().declareArgument(arguments, arg, 0))
        }
        return finalArguments
    }

}