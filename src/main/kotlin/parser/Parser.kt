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
        var currentPosition: Long = 0

        var i = 0
        // al principio i = 0, entonces el nuevo let está en 7 lugares --> 0 + 7 = 7
        // luego, el siguiente está en 7 luego --> 7 + 7 = 14
        while(i < tokenList.size) {
            val tokenInfo = tokenList[i]
            val token = tokenInfo.token
            val range = Range(0, 0)
            val astNode = parseByTokenType(tokenList, token, range, i)
            astNodes.add(astNode)
            i += lenghtOfDeclaration(tokenList, token, i)

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


    // ¿Eventualmente hacer un KeywordParser?
    fun  parseKeyword(tokens: List<TokenInfo>, token: Token, range: Range, i: Int): AST {
        return when (token.text) {
            "let" -> declareVariable(tokens, range, i)
            else -> throw Exception("Invalid keyword")
        }
    }

    fun declareVariable(tokens: List<TokenInfo>, range: Range, i: Int): Declaration{
        val variableDec = VariableDeclarator();
        return variableDec.declare(tokens, range, i)
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
