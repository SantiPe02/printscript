package parser
import ast.AST
import ast.Declaration
import ast.LiteralArgument
import ast.Range
import ast.Scope
import ast.VariableArgument
import token.TokenInfo
import token.TokenInfo.Token
import token.TokenInfo.TokenType

sealed interface Parser {
    fun parseTokens(tokenList: List<TokenInfo>): Result<AST>
}

class MyParser : Parser {
    private val commons = ParserCommons()

    override fun parseTokens(tokenList: List<TokenInfo>): Result<AST> {
        val astNodes = mutableListOf<AST>()
        var i = 0
        while (i < tokenList.size) {
            val tokenInfo = tokenList[i]
            val token = tokenInfo.token
            val astNode = parseByTokenType(tokenList, tokenInfo, i)
            astNode.onFailure {return astNode}
            astNode.onSuccess {astNodes.add(it)}
            val length = lengthOfDeclaration(tokenList, token, i)
            length.onSuccess {i += it}
            length.onFailure {return Result.failure(it)}
        }
        return Result.success(Scope("program", commons.getRangeOfTokenList(tokenList), astNodes))
    }

    private fun parseByTokenType(
        tokens: List<TokenInfo>,
        tokenInfo: TokenInfo,
        i: Int,
    ): Result<AST> {
        val token = tokenInfo.token
        return when (token.type) {
            TokenType.KEYWORD -> parseKeyword(tokens, tokenInfo, i)
            TokenType.SPECIAL_SYMBOL -> parseSpecial(token)
            TokenType.OPERATOR -> parseOperator(token)
            TokenType.IDENTIFIER -> parseIdentifier(tokens, tokenInfo, i)
            TokenType.LITERAL -> parseLiteral(token)
        }
    }

    // ¿Eventualmente hacer un KeywordParser?
    private fun parseKeyword(
        tokens: List<TokenInfo>,
        tokenInfo: TokenInfo,
        i: Int,
    ): Result<AST> {
        val token = tokenInfo.token
        return when (token.text) {
            // if it is not 'let', treat it like an identifier. e.g: object(); --> object works an identifier, cause it' an instance.
            "let" -> declareVariable(tokens, i)
            else -> parseIdentifier(tokens, tokenInfo, i)
        }
    }

    private fun declareVariable(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration> {
        val variableDec = VariableDeclarator()
        return variableDec.declare(tokens, i)
    }

    /**
     * Length refers to the amount of tokens of a specific declaration.
     * e.g: let name:String = "Carlos Salvador"; --> Length = 7
     * */
    private fun lengthOfDeclaration(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): Result<Int> {
        return when (token.type) {
            TokenType.KEYWORD -> lengthOfKeywordDeclaration(tokens, token, i)
            TokenType.IDENTIFIER -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
            else -> Result.success(1) // As for now...
        }
    }


    private fun lengthOfKeywordDeclaration(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): Result<Int> {
        return when (token.text) {
            "let" -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
            // acctually, should always return this, unless expected differenty, like classes
            else -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
        }
    }

    private fun parseLiteral(token: Token): Result<AST> {
        return Result.success(LiteralArgument(Range(0, 0), token.text, "String"))
    }

    // two cases, as for now
    // 1. used as a variable: a = 4;
    // 2. used as a isolated method declaration: println("Hello World");
    private fun parseIdentifier(
        tokens: List<TokenInfo>,
        tokenInfo: TokenInfo,
        i: Int,
    ): Result<AST> {
        // if it is a method call.
        if (tokens[i + 1].token.text == "(") {
            return handleMethodCall(tokens, tokenInfo, i)
        }

        return declareVariable(tokens, i - 1)
    }

    fun handleMethodCall(
        tokens: List<TokenInfo>,
        tokenInfo: TokenInfo,
        i: Int,
    ): Result<AST> {
        val methodDec = MethodResultDeclarator()
        val closingParenthesisIndex = commons.searchForClosingCharacter(tokens, "(", i + 1)
        closingParenthesisIndex.onSuccess {return methodDec.declareArgument(tokens, tokens.subList(i, it + 1), i)}
        return Result.failure(Exception("Error while parsing method call: Parenthesis not closed."))
    }

    // I don't think you can start with operators If you can't, this method should throw always error.
    private fun parseOperator(token: Token): Result<AST> {
        return Result.success(VariableArgument(Range(0, 0), token.text))
    }

    private fun parseSpecial(token: Token): Result<AST> {
        return Result.success(VariableArgument(Range(0, 0), token.text))
    }
}
