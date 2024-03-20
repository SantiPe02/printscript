package parser
import ast.*
import token.*
import token.TokenInfo.*
import java.util.*


sealed interface Parser {
    fun parseTokens(tokenList: List<TokenInfo>): Scope
}

class MyParser : Parser {

    val commons = ParserCommons();
    // position es para devolver el error, eg: error en linea 41, columna 9.
    override fun  parseTokens(tokenList: List<TokenInfo>): Scope {
        val astNodes = mutableListOf<AST>()
        var i = 0
        while(i < tokenList.size) {
            val tokenInfo = tokenList[i]
            val token = tokenInfo.token
            val astNode = parseByTokenType(tokenList, token, i)
            astNodes.add(astNode)
            i += lenghtOfDeclaration(tokenList, token, i)
        }
        return Scope("program", commons.getRangeOfTokenList(tokenList), astNodes)
    }


    fun parseByTokenType(tokens: List<TokenInfo>, token: Token,  i: Int): AST {
        return when (token.type) {
            TokenType.KEYWORD -> parseKeyword(tokens, token, i)
            TokenType.SPECIAL_SYMBOL -> parseSpecial(tokens, token, i)
            TokenType.OPERATOR -> parseOperator(tokens, token, i)
            TokenType.IDENTIFIER -> parseIdentifier(tokens, token, i)
            TokenType.LITERAL -> parseLiteral(tokens, token, i)
        }
    }


    // ¿Eventualmente hacer un KeywordParser?
    fun  parseKeyword(tokens: List<TokenInfo>, token: Token, i: Int): AST {
        return when (token.text) {
            "let" -> declareVariable(tokens, i)
            else -> throw Exception("Invalid keyword")
        }
    }

    fun declareVariable(tokens: List<TokenInfo>,  i: Int): Declaration{
        val variableDec = VariableDeclarator();
        return variableDec.declare(tokens,  i)
    }

    /**
     * Length refers to the amount of tokens of a specific declaration.
     * e.g: let name:String = "Carlos Salvador"; --> Length = 7
     * */
    fun lenghtOfDeclaration(tokens: List<TokenInfo>, token: Token, i: Int): Int {
        return when (token.type) {
            TokenType.KEYWORD -> lengthOfKeywordDeclaration(tokens, token, i)
            else -> 1 // As for now...
        }
    }

    private fun lengthOfKeywordDeclaration(tokens: List<TokenInfo>, token: Token, i: Int): Int {
        return when (token.text) {
            "let" -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";" , i)
            else -> 1 // As for now... (on classes it will search for a })
        }
    }


    private fun parseLiteral(tokens: List<TokenInfo>, token: TokenInfo.Token, i: Int): AST {
        return LiteralArgument(Range(0,0), token.text, "String")
    }

    private fun parseIdentifier(tokens: List<TokenInfo>, token: TokenInfo.Token, i: Int): AST {
        return VariableArgument(Range(0,0), token.text)
    }

    // I don't think you can start with operators If you cant, this method should throw always error.
    private fun parseOperator(tokens: List<TokenInfo>, token: TokenInfo.Token,  i: Int): AST {
        return VariableArgument(Range(0,0), token.text)
    }

    private fun parseSpecial(tokens: List<TokenInfo>, token: TokenInfo.Token, i: Int): AST {
        return VariableArgument(Range(0,0), token.text)
    }
}
