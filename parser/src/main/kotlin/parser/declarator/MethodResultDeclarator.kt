package parser.declarator

import ast.Argument
import ast.Call
import ast.MethodResult
import ast.Range
import parser.ParserCommons
import token.TokenInfo

class MethodResultDeclarator : ArgumentDeclarator {
    val commons = ParserCommons()

    override fun declareArgument(
        tokens: List<TokenInfo>,
        arguments: List<TokenInfo>,
        i: Int,
    ): Result<MethodResult> {
        val endIndex = commons.getEndOfVarIndex(tokens, i)
        val currentRange = commons.getRangeOfTokenList(arguments)
        val argument = methodArgument(tokens, i, endIndex, arguments, currentRange)

        return argument
    }

    fun methodArgument(
        tokens: List<TokenInfo>,
        i: Int,
        endIndex: Int,
        arguments: List<TokenInfo>,
        currentRange: Range,
    ): Result<MethodResult> {
        verifySyntax(tokens).onFailure { return Result.failure(it) }

        val operator: Result<TokenInfo>
        val finalMethodOp: Int

        val methodOperator: Result<Int> = commons.searchForFirstOperator(arguments, 0, arguments.size)

        methodOperator.onSuccess {
            finalMethodOp = it
            operator = getOperatorMethod(tokens, i + finalMethodOp)

            val args: Result<List<List<TokenInfo>>> = createMethodByOperator(tokens, i, endIndex, i + finalMethodOp)
            return handleArgAndOperatorResults(tokens, i, finalMethodOp, operator, args, arguments, currentRange)
        }

        return Result.failure(Exception("Method operator not found"))
    }

    // TODO: terminar
    fun handleArgAndOperatorResults(
        tokens: List<TokenInfo>,
        i: Int,
        methodOperator: Int,
        operator: Result<TokenInfo>,
        args: Result<List<List<TokenInfo>>>,
        arguments: List<TokenInfo>,
        currentRange: Range,
    ): Result<MethodResult> {
        args.onFailure { return Result.failure(it) }

        operator.onSuccess {
            val operatorValue: TokenInfo = it
            args.onSuccess {
                return createSuccsessfulMethodArgument(
                    tokens,
                    i,
                    methodOperator,
                    operatorValue,
                    it,
                    arguments,
                    currentRange,
                )
            }
        }

        return Result.failure(Exception("Operator not found"))
    }

    fun createSuccsessfulMethodArgument(
        tokens: List<TokenInfo>,
        i: Int,
        methodOperator: Int,
        operator: TokenInfo,
        args: List<List<TokenInfo>>,
        arguments: List<TokenInfo>,
        currentRange: Range,
    ): Result<MethodResult> {
        if (operator.token.text == "(") {
            return handleParenthesesOperator(args, currentRange)
        }
        if (hasEmptyArguments(tokens, i, methodOperator)) {
            return emptyMethodArgument(currentRange, operator)
        }
        val orderedArgs = sortTokenInfoListByPosition(args.flatten())
        val finalArguments = getFinalArgumentsOfMethodResult(args, arguments)
        finalArguments.onSuccess {
            val finalArgumentsList = it
            return Result.success(
                MethodResult(
                    commons.getRangeOfTokenList(listOf(operator)),
                    Call(commons.getRangeOfTokenList(orderedArgs), operator.token.text, finalArgumentsList),
                ),
            )
        }
        return Result.failure(Exception("Final arguments not found"))
    }

    fun hasEmptyArguments(
        tokens: List<TokenInfo>,
        i: Int,
        methodOperator: Int,
    ): Boolean {
        return (tokens[i + methodOperator].token.text == "(" && tokens[i + methodOperator + 1].token.text == ")")
    }

    fun sortTokenInfoListByPosition(tokens: List<TokenInfo>): List<TokenInfo> {
        return tokens.sortedBy { it.position.startIndex }
    }

    fun createMethodByOperator(
        tokens: List<TokenInfo>,
        i: Int,
        endIndex: Int,
        methodOperator: Int,
    ): Result<List<List<TokenInfo>>> {
        verifySyntax(tokens).onFailure { return Result.failure(it) }
        return when (tokens[methodOperator].token.text) {
            "+", "-", "*", "/" -> Result.success(separateArguments(tokens, i, endIndex, methodOperator))
            "(" -> getParenthesesArguments(tokens, methodOperator)
            else -> Result.failure(Exception("Invalid operator"))
        }
    }

    fun handleParenthesesOperator(
        args: List<List<TokenInfo>>,
        currentRange: Range,
    ): Result<MethodResult> {
        val flattenedArgs = args.flatten()
        return methodArgument(flattenedArgs, 0, flattenedArgs.size, flattenedArgs, currentRange)
    }

    fun separateArguments(
        tokens: List<TokenInfo>,
        i: Int,
        endIndex: Int,
        operatorIndex: Int,
    ): List<List<TokenInfo>> {
        val leftArgs = tokens.subList(i, operatorIndex)
        val rightArgs = tokens.subList(operatorIndex + 1, endIndex)
        return listOf(leftArgs, rightArgs)
    }

    // if the parenteses doesnt mark a method, make it call method argument recursively.
    fun getParenthesesArguments(
        tokens: List<TokenInfo>,
        methodOperator: Int,
    ): Result<List<List<TokenInfo>>> {
        // Actually, search for first operator, and work with that. If there is a single argument, return that element.
        val closingParentheses = commons.searchForClosingCharacter(tokens, "(", methodOperator)
        closingParentheses.onSuccess { return Result.success(listOf(tokens.subList(methodOperator + 1, it))) }
        return Result.failure(Exception("Closing parentheses not found"))
    }

    fun getOperatorMethod(
        tokens: List<TokenInfo>,
        methodOperator: Int,
    ): Result<TokenInfo> {
        return when (tokens[methodOperator].token.text) {
            "+", "-", "*", "/" -> Result.success(tokens[methodOperator])
            "(" -> Result.success(getOperatorWhenParentheses(tokens, methodOperator))
            else -> Result.failure(Exception("Todo"))
        }
    }

    fun getOperatorWhenParentheses(
        tokens: List<TokenInfo>,
        methodOperator: Int,
    ): TokenInfo {
        // two options, either it is test(args) or (args)
        if (methodOperator == 0) {
            return tokens[methodOperator] // NO. Call getOperatorMethod, with the inside values.
        }
        return when (tokens[methodOperator - 1].token.type) {
            TokenInfo.TokenType.OPERATOR -> {
                tokens[methodOperator]
            }
            else -> tokens[methodOperator - 1] // Same. Call getOperatorMethod, with the inside values.
        }
    }

    // MODULARIZAR ESTE MÉTODO.
    // The important part about this method is that I assure myself that there ain't no Results inside it.
    // either all fails, or all succedes.

    fun getFinalArgumentsOfMethodResult(
        args: List<List<TokenInfo>>,
        arguments: List<TokenInfo>,
    ): Result<List<Argument>> {
        val finalArguments: MutableList<Argument> = mutableListOf()
        for (arg in args) {
            if (arg.isEmpty()) {
                return Result.failure(Exception("Invalid sintax: there are no arguments on list"))
            } else if (hasCommasAsFirstTerm(arg)) {
                return hanldeCommaSeparatedArguments(arg, arguments, finalArguments) // it is a method with arguments
            } else if (arg.size != 1) {
                val methodArg = methodArgument(arg, 0, arg.size, arg, commons.getRangeOfTokenList(arg))
                methodArg.onFailure { return Result.failure(Exception("Method argument failed")) }
                methodArg.onSuccess { finalArguments.add(it) }
            } else {
                if (canAddArgumentsToList(arg, arguments)) {
                    addArgumentsToList(arg, arguments, finalArguments)
                } else {
                    return Result.failure(Exception("Invalid sintax: there are no arguments on list"))
                }
            }
        }
        return Result.success(finalArguments)
    }

    fun canAddArgumentsToList(
        arg: List<TokenInfo>,
        arguments: List<TokenInfo>,
    ): Boolean {
        val dec = VariableArgumentDeclarator().declareArgument(arguments, arg, 0)
        return dec.isSuccess
    }

    fun addArgumentsToList(
        arg: List<TokenInfo>,
        arguments: List<TokenInfo>,
        finalArguments: MutableList<Argument>,
    ) {
        val declaration = VariableArgumentDeclarator().declareArgument(arguments, arg, 0)
        declaration.onSuccess { finalArguments.add(it) }
    }

    fun hasCommasAsFirstTerm(args: List<TokenInfo>): Boolean {
        val firstTerms = commons.separateByFirstTerms(args, 0, args.size)
        firstTerms.onSuccess { return it.any { it.first.token.text == "," } }
        return false
    }

    fun hanldeCommaSeparatedArguments(
        args: List<TokenInfo>,
        arguments: List<TokenInfo>,
        finalArguments: MutableList<Argument>,
    ): Result<List<Argument>> {
        val canHandleCommas = canHandleCommaSeparation(args, arguments)
        canHandleCommas.onSuccess {
            if (it) {
                val newArgs = separateArgumentsByCommas(args)
                newArgs.onSuccess {
                    val finalArgs = getFinalArgumentsOfMethodResult(it, arguments)
                    finalArgs.onSuccess { it2 ->
                        finalArguments.addAll(it2)
                        return Result.success(finalArguments)
                    }
                }
            }
        }
        return Result.failure(Exception("Invalid syntax: cannot handle coma separated arguments"))
    }

    fun canHandleCommaSeparation(
        args: List<TokenInfo>,
        arguments: List<TokenInfo>,
    ): Result<Boolean> {
        val newArgs = separateArgumentsByCommas(args)
        newArgs.onSuccess {
            val finalArgs = getFinalArgumentsOfMethodResult(it, arguments)
            return Result.success(finalArgs.isSuccess)
        }

        return Result.failure(Exception("Invalid sintax: cannot handle coma separated arguments"))
    }

    fun separateArgumentsByCommas(args: List<TokenInfo>): Result<List<List<TokenInfo>>> {
        val newArgs: MutableList<List<TokenInfo>> = mutableListOf()
        val firstTerms: Result<List<Pair<TokenInfo, Int>>> = commons.separateByFirstTerms(args, 0, args.size)
        firstTerms.onSuccess {
            var start = 0
            for (arg in it) {
                if (arg.first.token.text == ",") {
                    newArgs.add(args.subList(start, arg.second))
                    start = arg.second + 1
                }
            }
            newArgs.add(args.subList(start, args.size))
            return Result.success(newArgs)
        }
        return Result.failure(Exception("Invalid syntax: cannot separate arguments by commas"))
    }

    fun emptyMethodArgument(
        range: Range,
        operator: TokenInfo,
    ): Result<MethodResult> {
        return Result.success(
            MethodResult(
                range,
                Call(range, operator.token.text, listOf()),
            ),
        )
    }

    /**
     * Invalid syntax cases:
     * Two consecutive LITERALS or IDENTIFIERS (or any combination between them)
     */

    private fun verifySyntax(tokens: List<TokenInfo>): Result<Boolean> {
        for (i in 0 until tokens.size - 1) {
            if (tokenIsLiteralOrIdentifierNorComma(tokens[i]) && tokenIsLiteralOrIdentifierNorComma(tokens[i + 1])) {
                return Result.failure(Exception("Invalid syntax: two consecutive literals"))
            }
        }
        return Result.success(true)
    }

    private fun tokenIsLiteralOrIdentifierNorComma(token: TokenInfo): Boolean {
        if (token.token.type == TokenInfo.TokenType.LITERAL || token.token.type == TokenInfo.TokenType.IDENTIFIER) {
            if (token.token.text != ",") {
                return true
            }
        }
        return false
    }
}
