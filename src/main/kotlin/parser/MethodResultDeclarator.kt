package parser

import ast.Argument
import ast.Call
import ast.MethodResult
import ast.Range
import token.TokenInfo

class MethodResultDeclarator : ArgumentDeclarator {
    val commons = ParserCommons();
    // 3 simple examples:

    /** 1- test(test1(leo + diego), boca) + 4*5
     *
    --> MethodResult(range, Call(range, "+", methodArgument(range, test(test1(leo + diego), boca)), methodArgument(range, 4*5))

    (4*5) is actually a simplification, it is actually a list of the tokens 4, *, 5

    2- 1 + 2 + 3 --> MethodResult(range, Call(range, "+", LiteralArg(range, "int", "1"), methodArgument(range, 2 + 3))

    3- a + 3 --> MethodResult(range, Call(range, "+", VariableArgument(range, "a"), LiteralArgument(range, "int", "3"))

    if left or right arg is a literal or variable --> solve.
    else: recursive call to methodArgument*/

    override fun declareArgument(range: Range, tokens: List<TokenInfo>, arguments: List<TokenInfo>, i: Int): MethodResult{
        val endIndex = commons.getEndOfVarIndex(tokens, i)
        val argument = methodArgument(range, tokens, i, endIndex, arguments)

        return argument
    }

    // i: where it starts, in this case it is relative, it is not necessarily the same i used in parseTokens()
    fun methodArgument(range: Range, tokens: List<TokenInfo>, i: Int, endIndex: Int, arguments: List<TokenInfo>): MethodResult {
        val methodOperator: Int = commons.searchForFirstOperator(arguments, 0, arguments.size)
        val args: List<List<TokenInfo>> = createMethodByOperator(range, tokens, i, endIndex, i+methodOperator)
        val operator: TokenInfo = getOperatorMethod(tokens, i, i+methodOperator)

        val finalArguments = getFinalArgumentsOfMethodResult(args, range, arguments)
        return MethodResult(range, Call(range, operator.token.text, finalArguments))
    }
    fun createMethodByOperator(range:Range, tokens: List<TokenInfo>, i: Int, endIndex: Int, methodOperator: Int): List<List<TokenInfo>> {
        return when(tokens[methodOperator].token.text){
            "+", "-", "*", "/" -> separateArguments(range, tokens, i, endIndex, methodOperator)
            // if "(" revisar si es llamado a un método o calculo combinado.
            // (si el token anterior al parentesis es de tipo variable entonces es llamada a un metodo)
            "(" -> throw Exception("Todo")
            else -> throw Exception("Invalid operator")
        }
    }

    fun separateArguments(range:Range, tokens: List<TokenInfo>, i: Int, endIndex: Int, operatorIndex: Int): List<List<TokenInfo>> {
        val leftArgs = tokens.subList(i, operatorIndex)
        val rightArgs = tokens.subList(operatorIndex + 1, endIndex)
        return listOf(leftArgs, rightArgs)
    }
    fun getOperatorMethod(tokens: List<TokenInfo>, i: Int, methodOperator: Int): TokenInfo{
        return when(tokens[methodOperator].token.text){
            "+", "-", "*", "/" -> return tokens[methodOperator]
            else -> throw Exception("Todo")
        }
    }

    fun getFinalArgumentsOfMethodResult(args: List<List<TokenInfo>>, range: Range, arguments: List<TokenInfo>): List<Argument> {
        val finalArguments: MutableList<Argument> = mutableListOf()
        for (arg in args) {
            if(arg.isEmpty())
                throw Exception("Invalid sintax: there are no arguments on list")
            else if (arg.size != 1){
                finalArguments.add(methodArgument(range, arg, 0, arg.size , arg))}
            else
                finalArguments.add(VariableArgumentDeclarator().declareArgument(range, arguments, arg, 0))
        }
        return finalArguments
    }

}