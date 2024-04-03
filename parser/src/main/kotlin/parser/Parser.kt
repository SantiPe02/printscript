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
    override fun  parseTokens(tokenList: List<TokenInfo>): Scope {
        val astNodes = mutableListOf<AST>()
        var i = 0
        while (i < tokenList.size) {
            val tokenInfo = tokenList[i]
            val token = tokenInfo.token
            val astNode = parseByTokenType(tokenList, token, i)
            astNodes.add(astNode)
            i += lenghtOfDeclaration(tokenList, token, i)
        }
        return Scope("program", commons.getRangeOfTokenList(tokenList), astNodes)
    }

    private fun parseByTokenType(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): AST {
        return when (token.type) {
            TokenType.KEYWORD -> parseKeyword(tokens, token, i)
            TokenType.SPECIAL_SYMBOL -> parseSpecial(token)
            TokenType.OPERATOR -> parseOperator(token)
            TokenType.IDENTIFIER ->  parseIdentifier(tokens, token, i)
            TokenType.LITERAL -> parseLiteral(token)
        }
    }

    // ¿Eventualmente hacer un KeywordParser?
    private fun parseKeyword(
        tokens: List<TokenInfo>,
        token: Token,
        i: Int,
    ): AST {
        return when (token.text) {
            "let" -> declareVariable(tokens, i)
            else -> throw Exception("Invalid keyword")
        }
    }

    private fun declareVariable(
        tokens: List<TokenInfo>,
        i: Int,
    ): Declaration {
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
            TokenType.IDENTIFIER -> commons.lengthTillFirstAppearanceOfToken(tokens, TokenType.SPECIAL_SYMBOL, ";" , i)
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
            else -> 1 // As for now... (on classes it will search for a })
        }
    }

    private fun parseLiteral(token: Token): AST {
        return LiteralArgument(Range(0, 0), token.text, "String")
    }

    private fun parseIdentifier(tokens: List<TokenInfo>, token: Token, i: Int): AST {
        // As for now, the only way an identifier can be used is as a variable: a = 4;
        return declareVariable(tokens, i-1);
    }

    // I don't think you can start with operators If you cant, this method should throw always error.
    private fun parseOperator(token: Token): AST {
        return VariableArgument(Range(0, 0), token.text)
    }

    private fun parseSpecial(token: Token): AST {
        return VariableArgument(Range(0, 0), token.text)
    }
}
