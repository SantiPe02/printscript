package parser
import ast.AST
import ast.Conditional
import ast.Declaration
import ast.LiteralArgument
import ast.Range
import ast.VariableArgument
import parser.declarator.ConditionalDeclarator
import parser.declarator.MethodResultDeclarator
import parser.declarator.VariableDeclarator
import token.TokenInfo

class ParserDeclarator {
    val commons = ParserCommons()

    fun parseByTokenType(
        tokens: List<TokenInfo>,
        tokenInfo: TokenInfo,
        i: Int,
    ): Result<AST> {
        val token = tokenInfo.token
        return when (token.type) {
            TokenInfo.TokenType.KEYWORD -> parseKeyword(tokens, tokenInfo, i)
            TokenInfo.TokenType.SPECIAL_SYMBOL -> parseSpecial(token)
            TokenInfo.TokenType.OPERATOR -> parseOperator(token)
            TokenInfo.TokenType.IDENTIFIER -> parseIdentifier(tokens, i)
            TokenInfo.TokenType.LITERAL -> parseLiteral(token)
        }
    }

    // if it is not 'let', treat it like an identifier. e.g: object(); --> object works an identifier, cause it' an instance.
    private fun parseKeyword(
        tokens: List<TokenInfo>,
        tokenInfo: TokenInfo,
        i: Int,
    ): Result<AST> {
        val token = tokenInfo.token
        return when (token.text) {
            "let" -> declareVariable(tokens, i)
            "const" -> declareVariable(tokens, i)
            "if" -> declareConditional(tokens, i)
            else -> parseIdentifier(tokens, i)
        }
    }

    private fun declareConditional(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Conditional> {
        return ConditionalDeclarator().declareIf(tokens, i)
    }

    private fun declareVariable(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Declaration> {
        val variableDec = VariableDeclarator()
        return variableDec.declare(tokens, i)
    }

    private fun parseLiteral(token: TokenInfo.Token): Result<AST> {
        return Result.success(LiteralArgument(Range(0, 0), token.text, "String"))
    }

    // two cases, as for now
    // 1. used as a variable: a = 4;
    // 2. used as a isolated method declaration: println("Hello World");
    private fun parseIdentifier(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<AST> {
        // if it is a method call.
        if (tokens[i + 1].token.text == "(") {
            return handleMethodCall(tokens, i)
        }

        return declareVariable(tokens, i)
    }

    fun handleMethodCall(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<AST> {
        val methodDec = MethodResultDeclarator()
        val closingParenthesisIndex = commons.searchForClosingCharacter(tokens, "(", i + 1)
        closingParenthesisIndex.onSuccess { return methodDec.declareArgument(tokens, tokens.subList(i, it + 1), i) }
        return Result.failure(Exception("Error while parsing method call: Parenthesis not closed."))
    }

    // I don't think you can start with operators If you can't, this method should throw always error.
    private fun parseOperator(token: TokenInfo.Token): Result<AST> {
        return Result.success(VariableArgument(Range(0, 0), token.text))
    }

    private fun parseSpecial(token: TokenInfo.Token): Result<AST> {
        return Result.success(VariableArgument(Range(0, 0), token.text))
    }
}
