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
    fun parseTokens(tokenList: List<TokenInfo>): Scope
}

class MyParser : Parser {
    private val commons = ParserCommons()

    override fun parseTokens(tokenList: List<TokenInfo>): Scope {
        val astNodes = mutableListOf<AST>()
        var i = 0
        while (i < tokenList.size) {
            val tokenInfo = tokenList[i]
            val token = tokenInfo.token
            val astNode = parseByTokenType(tokenList, tokenInfo, i)
            astNodes.add(astNode)
            i += lenghtOfDeclaration(tokenList, token, i)
        }
        return Scope("program", commons.getRangeOfTokenList(tokenList), astNodes)
    }

    private fun parseByTokenType(
        tokens: List<TokenInfo>,
        tokenInfo: TokenInfo,
        i: Int,
    ): AST {
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
    private fun parseKeyword(tokens: List<TokenInfo>, tokenInfo: TokenInfo, i: Int): AST {
        val token = tokenInfo.token
        return when (token.text) {
            // if it is not 'let', treat it like an identifier. e.g: object(); --> object works an identifier, cause it' an instance.
            "let" -> declareVariable(tokens, i)
            else -> parseIdentifier(tokens, tokenInfo, i)
        }
    }

    private fun declareVariable(tokens: List<TokenInfo>, i: Int): Declaration {
        val variableDec = VariableDeclarator()
        return variableDec.declare(tokens, i)
    }

    /**
     * Length refers to the amount of tokens of a specific declaration.
     * e.g: let name:String = "Carlos Salvador"; --> Length = 7
     * */
    private fun lenghtOfDeclaration(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): Int {
        return when (token.type) {
            TokenType.KEYWORD -> lengthOfKeywordDeclaration(tokens, token, i)
            TokenType.IDENTIFIER -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
            else -> 1 // As for now...
        }
    }

    private fun lengthOfKeywordDeclaration(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): Int {
        return when (token.text) {
            "let" -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i)
            else -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";", i) // acctually, should always return this, unless expected differenty, like classes
        }
    }

    private fun parseLiteral(token: Token): AST {
        return LiteralArgument(Range(0, 0), token.text, "String")
    }


    // two cases, as for now
    // 1. used as a variable: a = 4;
    // 2. used as a isolated method declaration: println("Hello World");
    private fun parseIdentifier(tokens: List<TokenInfo>, tokenInfo: TokenInfo, i: Int): AST {

        // if it is a method call.
        if(tokens[i+1].token.text == "(") {
            return handleMethodCall(tokens, tokenInfo, i)
        }

        return declareVariable(tokens, i - 1)
    }

    // ESTO ESTÁ MAL, PORQUE Lo tengo q llamar para cada caso
    // tanto para let... method()
    // como para method() así sin más.
    fun handleMethodCall(tokens: List<TokenInfo>, tokenInfo: TokenInfo, i: Int): AST{
        val methodDec = MethodResultDeclarator()

        val closingParenthesisIndex = commons.searchForClosingCharacter(tokens, "(", i+1)
        return methodDec.declareArgument(tokens, tokens.subList(i, closingParenthesisIndex + 1), i)
    }

    // I don't think you can start with operators If you cant, this method should throw always error.
    private fun parseOperator(token: Token): AST {
        return VariableArgument(Range(0, 0), token.text)
    }

    private fun parseSpecial(token: Token): AST {
        return VariableArgument(Range(0, 0), token.text)
    }
}
