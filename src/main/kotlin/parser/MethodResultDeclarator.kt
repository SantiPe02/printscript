package parser

import ast.Argument
import ast.Call
import ast.MethodResult
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
        val operator: TokenInfo = getOperatorMethod(tokens, i, i + methodOperator)
        val args: List<List<TokenInfo>> = createMethodByOperator(tokens, i, endIndex, i + methodOperator)
        if(operator.token.text == "(")
            return handleParenthesesOperator(args)

        val orderedArgs = sortTokenInfoListByPosition(args.flatten())
        val finalArguments = getFinalArgumentsOfMethodResult(args, arguments)
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
    fun handleParenthesesOperator(args: List<List<TokenInfo>>) : MethodResult{
        val flattenedArgs = args.flatten()
        return methodArgument(flattenedArgs, 0, flattenedArgs.size, flattenedArgs)
    }

    fun separateArguments(tokens: List<TokenInfo>, i: Int, endIndex: Int, operatorIndex: Int): List<List<TokenInfo>> {
        val leftArgs = tokens.subList(i, operatorIndex)
        val rightArgs = tokens.subList(operatorIndex + 1, endIndex)
        return listOf(leftArgs, rightArgs)
    }

    // if the parenteses doesnt mark a method, make it call method argument recursively.
    fun getParenthesesArguments( tokens: List<TokenInfo>, methodOperator: Int): List<List<TokenInfo>> {
        // Actually, search for first operator, and work with that. If there is a single argument, return that element.
        val closingParentheses = commons.searchForClosingCharacter(tokens, "(", methodOperator)
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
            return tokens[methodOperator] // NO. Call getOperatorMethod, with the inside values.
        return when(tokens[methodOperator-1].token.type){
            TokenInfo.TokenType.IDENTIFIER -> {tokens[methodOperator-1]}
            else -> tokens[methodOperator] // Same. Call getOperatorMethod, with the inside values.
        }
    }

    fun getFinalArgumentsOfMethodResult(args: List<List<TokenInfo>>, arguments: List<TokenInfo>): List<Argument> {
        val finalArguments: MutableList<Argument> = mutableListOf()
        for (arg in args) {
            if(arg.isEmpty()) throw Exception("Invalid sintax: there are no arguments on list")
            else if(hasCommasAsFirstTerm(arg)) hanldeCommaSeparatedArguments(arg, arguments, finalArguments) // it is a method with arguments
            else if (arg.size != 1) finalArguments.add(methodArgument( arg, 0, arg.size , arg)) // if the size of an arguments is more than 1 then it means there's
            else addArgumentsToList(arg, arguments, finalArguments)
        }
        return finalArguments
    }


    fun addArgumentsToList(arg: List<TokenInfo>, arguments: List<TokenInfo>, finalArguments: MutableList<Argument>){
        finalArguments.add(VariableArgumentDeclarator().declareArgument(arguments, arg, 0))
    }

    fun hasCommasAsFirstTerm(args: List<TokenInfo>): Boolean{
        val firstTerms = commons.separateByFirstTerms(args, 0, args.size)
        return firstTerms.any { it.first.token.text == "," }
    }

    fun hanldeCommaSeparatedArguments(args: List<TokenInfo>, arguments: List<TokenInfo>, finalArguments: MutableList<Argument>){
        val newArgs = separateArgumentsByCommas(args);
        finalArguments.addAll(getFinalArgumentsOfMethodResult(newArgs, arguments))
    }

    fun separateArgumentsByCommas(args: List<TokenInfo>): List<List<TokenInfo>>{
        val newArgs: MutableList<List<TokenInfo>> = mutableListOf()
        val firstTerms: List<Pair<TokenInfo, Int>> = commons.separateByFirstTerms(args, 0, args.size)
        var start = 0
        for(arg in firstTerms){
            if(arg.first.token.text == ","){
                newArgs.add(args.subList(start, arg.second))
                start = arg.second+1
            }
        }
        newArgs.add(args.subList(start, args.size))
        return newArgs
    }
}