package parser
import ast.*
import token.*
import token.TokenInfo.*


sealed interface Parser {
    fun parseTokens(tokenList: List<TokenInfo>): Scope
}

class MyParser : Parser {

    // position es para devolver el error, eg: error en linea 41, columna 9.
    override fun  parseTokens(tokenList: List<TokenInfo>): Scope {
        val astNodes = mutableListOf<AST>()
        var currentPosition: Long = 0

        var i = 0
        while(i < tokenList.size) {
            val tokenInfo = tokenList[i]
            val token = tokenInfo.token
            val range = Range(0, 0)
            val astNode = parseByTokenType(tokenList, token, range, i)
            astNodes.add(astNode)
            i += lenghtOfDeclaration(tokenList, token, i) // -1 (?)
        }

        // I'm not yet handling possition right. But the tokens already bring their positions.
        // the range below starts from 0 till the end...
        // current position is the position of the last of the tokens I visited. Easy as fuck.
        return Scope("program", Range(0, currentPosition), astNodes)
    }

    fun parseByTokenType(tokens: List<TokenInfo>, token: Token, range: Range, i: Int): AST {
        return when (token.type) {
            TokenType.KEYWORD -> parseKeyword(tokens, token, range, i)
            TokenType.SPECIAL_SYMBOL -> parseSpecial(tokens, token, range, i)
            TokenType.OPERATOR -> parseOperator(tokens, token, range, i)
            TokenType.IDENTIFIER -> parseIdentifier(tokens, token, range, i)
            TokenType.LITERAL -> parseLiteral(tokens, token, range, i)
        }
    }

    fun  parseKeyword(tokens: List<TokenInfo>, token: Token, range: Range, i: Int): AST {
        return when (token.text) {
            "let" -> declareVariable(tokens, range, i)
            else -> throw Exception("Invalid keyword")
        }
    }

    /**
     * Length refers to the amount of tokens of a specific declaration.
     * e.g: let name:String = "Carlos Salvador"; --> Length = 7
     * */
    fun lenghtOfDeclaration(tokens: List<TokenInfo>, token: Token, i: Int): Int {
        return when (token.type) {
            TokenType.KEYWORD -> lengthOfDeclaration(tokens, token, i)
            else -> 1 // As for now...
        }
    }

    private fun lengthOfDeclaration(tokens: List<TokenInfo>, token: Token, i: Int): Int {
        return when (token.text) {
            "let" -> lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";" , i)
            else -> 1 // As for now... (on classes it will search for a })
        }
    }

    private fun lengthTillFirstAppearanceOfToken(tokens: List<TokenInfo>, type: TokenType, tokenText: String, i: Int): Int {
        var j = i
        while (j < tokens.size) {
            val token = tokens[j].token
            if (token.type == type && token.text == tokenText) {
                return j
            }
            j++
        }
        throw Exception("Error: Missing $tokenText")
    }



    /* todo: verify arguments are valid (eg:no "=" operator accepted)
       Brackets should close.
       Only an operator is not valid, () same, 1 + ; not valid as well
       --> that is the job of a method, to see if the + method has the correct input args.
    */

    fun declareVariable(tokens: List<TokenInfo>, range: Range, i: Int): Declaration
    {
        var j = i
        val variableName = getTokenByType(tokens[++j].token, TokenType.IDENTIFIER)
        val typeSpecialSymbol = getTokenByTextAndType(tokens[++j].token, ":", TokenType.SPECIAL_SYMBOL)
        val variableType = getTokenByType(tokens[++j].token, TokenType.IDENTIFIER)
        val equalOperator = getTokenByTextAndType(tokens[++j].token, "=", TokenType.OPERATOR)
        val k = j
        val arguments = getVariableArguments(tokens, ++j) // arguments could be empty, which is the same to saying let name:String;

        return when(arguments.size){
            1 -> declareByArgumentType(range, variableName.text, variableType.text, arguments[0])
            else -> declareMethodArgument(range, tokens, variableName.text, variableType.text, arguments, k)
        }
    }

    fun getTokenByType(token: Token, type: TokenType): Token {
        if(token.type != type)
            throw Exception("Invalid sintax: token should be of type $type")
        return token
    }

    fun getTokenByText(token: Token, text: String): Token {
        if(token.text != text)
            throw Exception("Invalid syntax: \"$text\" missing")
        return token
    }

    fun getTokenByTextAndType(token: Token, text: String, type: TokenType): Token {
        // if by getting token by type and name and no error is thrown --> then it is valid
        getTokenByType(token, type)
        getTokenByText(token, text)
        return token
    }

    fun isOfType(token: Token, type: TokenType): Boolean {
        return token.type == type
    }


    fun getVariableArguments(tokens: List<TokenInfo>, i: Int): List<TokenInfo> {
        val arguments = mutableListOf<TokenInfo>()
        var j = i
        while(j < tokens.size) // si hay una suma o algo raro repetir los métodos y así...
        {
            if(isEndOfVarChar(tokens, j))
                break
            else
                arguments.add(tokens[j])
            j++
        }
        return arguments
    }

    fun isEndOfVarChar(tokens: List<TokenInfo>, i: Int): Boolean{
        if(tokens[i].token.text == ";")
            return true
        return false
    }
    fun declareByArgumentType(range: Range, varName: String, varType: String, argument: TokenInfo): Declaration {
        // methods, on smaller cases, have three arguments: methodName, "(" and ")": they don't fit here.
        return when(argument.token.type){
            TokenType.LITERAL  -> VariableDeclaration(range, varName, varType, LiteralArgument(range, argument.token.text, argument.token.type.name))
            TokenType.IDENTIFIER -> VariableDeclaration(range, varName, varType, VariableArgument(range, argument.token.text))
            else -> throw Exception("Invalid sintax: there should be a single argument after the equal operator")
        }
    }


    // 3 simple examples:

    /** 1- test(test1(leo + diego), boca) + 4*5
     *
    --> MethodResult(range, Call(range, "+", methodArgument(range, test(test1(leo + diego), boca)), methodArgument(range, 4*5))

    (4*5) is actually a simplification, it is actually a list of the tokens 4, *, 5

    2- 1 + 2 + 3 --> MethodResult(range, Call(range, "+", LiteralArg(range, "int", "1"), methodArgument(range, 2 + 3))

    3- a + 3 --> MethodResult(range, Call(range, "+", VariableArgument(range, "a"), LiteralArgument(range, "int", "3"))

    if left or right arg is a literal or variable --> solve.
    else: recursive call to methodArgument*/

    fun declareMethodArgument(range: Range, tokens: List<TokenInfo>, varName: String, varType: String, arguments: List<TokenInfo>, i: Int): Declaration{
        val endIndex = getEndOfVarIndex(tokens, i)
        val argument = methodArgument(range, tokens, i, endIndex)
        return VariableDeclaration(range, varName, varType, argument)
    }


    // i: where it starts, in this case it is relative, it is not necessarily the same i used in parseTokens()
    fun methodArgument(range: Range, tokens: List<TokenInfo>, i: Int, endIndex: Int): Argument {

        val methodOperator: Int = searchForOperator(tokens, i, endIndex)
        val args: List<List<TokenInfo>> = createMethodByOperator(tokens, i, endIndex, methodOperator)
        val operator: TokenInfo = getOperatorMethod(tokens, i, methodOperator)

        val finalArguments = getFinalArgumentsOfMethodResult(args, range)

        return MethodResult(range, Call(range, operator.token.text, finalArguments))
    }

    fun getFinalArgumentsOfMethodResult(args: List<List<TokenInfo>>, range: Range): List<Argument> {
        val finalArguments: MutableList<Argument> = mutableListOf()
        for (arg in args) {
            if (arg.size != 1) {
                finalArguments.add(methodArgument(range, arg, 0, arg.size))
            } else
                finalArguments.add(argumentByType(arg[0], range))
        }
        return finalArguments
    }


    fun argumentByType(arg: TokenInfo, range: Range): Argument {
        return when(arg.token.type){
            TokenType.LITERAL -> LiteralArgument(range, arg.token.text, arg.token.type.name)
            TokenType.IDENTIFIER -> VariableArgument(range, arg.token.text)
            else -> throw Exception("Invalid argument type")
        }
    }

    fun searchForOperator(tokens: List<TokenInfo>, i: Int, endIndex: Int): Int {
        // first searchs for + or - operators,
        // then for * or / operators
        // finaly, for ( or ) operators (usualy used in methods: test(1, 3)...)
        val firstTerms: List<TokenInfo> = separateByFirstTerms(tokens, i, endIndex)
        return getHighestLevelMethod(firstTerms, i)
    }

    fun createMethodByOperator(tokens: List<TokenInfo>, i: Int, endIndex: Int, methodOperator: Int): List<List<TokenInfo>> {
        return when(tokens[i].token.text){
            "+", "-", "*", "/" -> separateArguments(tokens, i, endIndex, methodOperator)
            // if "(" revisar si es llamado a un método o calculo combinado.
            // (si el token anterior al parentesis es de tipop variable entonces es llamada a un metodo)
            "(" -> throw Exception("Todo")
            else -> throw Exception("Invalid operator")
        }
    }

    fun getOperatorMethod(tokens: List<TokenInfo>, i: Int, methodOperator: Int): TokenInfo{
        return when(tokens[methodOperator].token.text){
            "+", "-", "*", "/" -> return tokens[methodOperator]
            else -> throw Exception("Todo")
        }
    }

    fun separateArguments(tokens: List<TokenInfo>, i: Int, endIndex: Int, operatorIndex: Int): List<List<TokenInfo>> {
        val leftArgs = tokens.subList(i, operatorIndex)
        val rightArgs = tokens.subList(operatorIndex + 1, endIndex)
        return listOf(leftArgs, rightArgs)
    }


    fun getHighestLevelMethod(firstTerm: List<TokenInfo>, i: Int): Int{
        var j: Int = 0
        var firstMultOrDiv = -1
        var firstParenthesis = -1
        for(token in firstTerm){
            if(token.token.text == "+" || token.token.text == "-")
                return i + j
            else if(token.token.text == "*" || token.token.text == "/" && firstMultOrDiv == -1)
                firstMultOrDiv = i + j
            else if(token.token.text == "(" && firstParenthesis == -1)
                firstParenthesis = i + j
            j++
        }
        return if(firstMultOrDiv != -1) firstMultOrDiv else firstParenthesis
    }

    fun getEndOfVarIndex(tokens: List<TokenInfo>, i: Int): Int {
        var j = i
        while(j < tokens.size) // si hay una suma o algo raro repetir los métodos y así...
        {
            if(isEndOfVarChar(tokens, j))
                return j
            j++
        }
        return j
    }

    // in test(test1(leo + diego), boca) + 4*5
    // the "first" terms are: test, +, 4, *, 5

    fun separateByFirstTerms(tokens: List<TokenInfo>, i: Int, end: Int): List<TokenInfo>{
        var j = i
        val firstTerms = mutableListOf<TokenInfo>()
        while(j < end) // si hay una suma o algo raro repetir los métodos y así...
        {
            if(isOpeningChar(tokens[j].token.text))
                j = searchForClosingCharacter(tokens, tokens[j].token.text, j)
            else{
                firstTerms.add(tokens[j])
                j++
            }
        }
        return firstTerms
    }

    fun isOpeningChar(char: String): Boolean {
        return char == "(" || char == "{" || char == "["
    }

    // in (Brujita(Chapu(Gata))) The closing char of the first "(" is the last ")".
    fun searchForClosingCharacter(tokens: List<TokenInfo>, tokenText: String, i: Int): Int {
        val closingCharText = oppositeChar(tokenText)
        var appsOfTokenText = 0 // times same token appears before the closing one.
        var j = i
        while (j < tokens.size) {
            val token = tokens[j].token
            if (token.type == TokenType.SPECIAL_SYMBOL){
                if(token.text == closingCharText){
                    if (appsOfTokenText == 0)
                        return j
                    else
                        appsOfTokenText--
                }
                else if(token.text == tokenText)
                    appsOfTokenText++
            }
            j++
        }
        throw Exception("Error: Missing closing bracket")
    }

    fun oppositeChar(char: String): String {
        return when (char) {
            "(" -> ")"
            "{" -> "}"
            "[" -> "]"
            else -> throw Exception("Invalid character")
        }
    }

    private fun parseLiteral(tokens: List<TokenInfo>, token: TokenInfo.Token, range: Range, i: Int): AST {
        return LiteralArgument(range, token.text, "String")
    }

    private fun parseIdentifier(tokens: List<TokenInfo>, token: TokenInfo.Token, range: Range, i: Int): AST {
        return VariableArgument(range, token.text)
    }

    // I don't think you can start with operators If you cant, this method should throw always error.
    private fun parseOperator(tokens: List<TokenInfo>, token: TokenInfo.Token, range: Range, i: Int): AST {
        return VariableArgument(range, token.text)
    }

    private fun parseSpecial(tokens: List<TokenInfo>, token: TokenInfo.Token, range: Range, i: Int): AST {
        return VariableArgument(range, token.text)
    }
}
