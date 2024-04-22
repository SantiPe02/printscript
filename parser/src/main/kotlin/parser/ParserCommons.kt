package parser

import ast.Range
import token.TokenInfo
import java.util.Locale

/**The name is not convenient, ParserCommons is a class that handles working with tokens.*/
class ParserCommons {
    fun getTokenByType(
        token: TokenInfo.Token,
        type: TokenInfo.TokenType,
    ): Result<TokenInfo.Token> {
        if (token.type != type) {
            return Result.failure(Exception("Invalid syntax: token type: ${token.type} but expected type is: $type"))
        }
        return Result.success(token)
    }

    fun getTokenByText(
        token: TokenInfo.Token,
        text: String,
    ): Result<TokenInfo.Token> {
        if (token.text != text) {
            return Result.failure(Exception("Invalid syntax: \"$text\" missing"))
        }
        return Result.success(token)
    }

    fun getTokenByTextAndType(
        token: TokenInfo.Token,
        text: String,
        type: TokenInfo.TokenType,
    ): TokenInfo.Token {
        // if by getting token by type and name and no error is thrown --> then it is valid
        getTokenByType(token, type)
        getTokenByText(token, text)
        return token
    }

    fun isOfType(
        token: TokenInfo.Token,
        type: TokenInfo.TokenType,
    ): Boolean {
        return token.type == type
    }

    fun isEndOfVarChar(
        tokens: List<TokenInfo>,
        i: Int,
    ): Boolean {
        if (tokens[i].token.text == ";") {
            return true
        }
        return false
    }

    fun getEndOfVarIndex(
        tokens: List<TokenInfo>,
        i: Int,
    ): Int {
        var j = i
        while (j < tokens.size) // si hay una suma o algo raro repetir los métodos y así...
        {
            if (isEndOfVarChar(tokens, j)) {
                return j
            }
            j++
        }
        return j
    }

    fun searchForFirstOperator(
        tokens: List<TokenInfo>,
        i: Int,
        endIndex: Int,
    ): Result<Int> {
        val firstTerms: Result<List<Pair<TokenInfo, Int>>> = separateByFirstTerms(tokens, i, endIndex)
        firstTerms.onSuccess { return getHighestLevelMethod(it, i) }
        return Result.failure(Exception("Invalid syntax: missing operator"))
    }

    // in test(test1(leo + diego), boca) + 4*5
    // the "first" terms are: test, +, 4, *, 5
    fun separateByFirstTerms(
        tokens: List<TokenInfo>,
        i: Int,
        end: Int,
    ): Result<List<Pair<TokenInfo, Int>>> {
        var j = i
        val firstTerms = mutableListOf<Pair<TokenInfo, Int>>() // Modified to hold pairs
        while (j < end) {
            if (isOpeningChar(tokens[j].token.text)) {
                firstTerms.add(Pair(tokens[j], j))
                val closingChar = searchForClosingCharacter(tokens, tokens[j].token.text, j)
                closingChar.onSuccess { j = it + 1 }
                closingChar.onFailure { return Result.failure(it) }
            } else {
                // Create a pair with the current token and its index
                firstTerms.add(Pair(tokens[j], j))
                j++
            }
        }
        return Result.success(firstTerms)
    }

    fun isOpeningChar(char: String): Boolean {
        return char == "(" || char == "{" || char == "["
    }

    /**In 4 * 5 + 5/8 - 3 the highest level method is the "+", the second is the "-" the third the "*" and fourth "/"
     This method could eventualy be extensible for lists, like [].
     */

    fun getHighestLevelMethod(
        firstTerm: List<Pair<TokenInfo, Int>>,
        i: Int,
    ): Result<Int> {
        var firstMultOrDiv = -1
        var firstParenthesis = -1
        for (token in firstTerm) {
            val t = token.first
            if (t.token.text == "+" || t.token.text == "-") {
                return Result.success(i + token.second)
            } else if (t.token.text == "*" || t.token.text == "/" && firstMultOrDiv == -1) {
                firstMultOrDiv = i + token.second
            } else if (t.token.text == "(" && firstParenthesis == -1) {
                firstParenthesis = i + token.second
            }
        }
        if (firstMultOrDiv != -1) {
            return Result.success(firstMultOrDiv)
        } else if (firstParenthesis != -1) {
            return Result.success(firstParenthesis)
        }

        return Result.failure(Exception("There is no operation left in the argument"))
    }

    // in (Brujita(Chapu(Gata))) The closing char of the first "(" is the last ")".
    fun searchForClosingCharacter(
        tokens: List<TokenInfo>,
        tokenText: String,
        i: Int,
    ): Result<Int> {
        val closingCharText = oppositeChar(tokenText)
        closingCharText.onSuccess {
            var appsOfTokenText = 0 // times same token appears before the closing one.
            var j = i + 1
            while (j < tokens.size) {
                val token = tokens[j].token
                if (token.type == TokenInfo.TokenType.SPECIAL_SYMBOL) {
                    if (token.text == it) {
                        if (appsOfTokenText == 0) {
                            return Result.success(j)
                        } else {
                            appsOfTokenText--
                        }
                    } else if (token.text == tokenText) {
                        appsOfTokenText++
                    }
                }
                j++
            }
        }

        return Result.failure(Exception("Error: Missing closing bracket"))
    }

    fun oppositeChar(char: String): Result<String> {
        return when (char) {
            "(" -> Result.success(")")
            "{" -> Result.success("}")
            "[" -> Result.success("]")
            else -> Result.failure(Exception("Invalid character"))
        }
    }

    fun getDataTypeFromString(word: String): String {
        return when {
            word.all { it.isDigit() } -> "number"
            word.matches(Regex("-?\\d+(\\.\\d+)?")) -> "number"
            word.lowercase(Locale.getDefault()) == "true" || word.lowercase(Locale.getDefault()) == "false" -> "boolean"
            else -> "string"
            // Todo: when it is a method, the type is the return value of that method.S
            //  Lists are methods, for example: list(1,2,3)... unless they ask otherwise
            // else -> throw Exception("Todo: data type not handled yet")
        }
    }

    fun lengthTillFirstAppearanceOfToken(
        tokens: List<TokenInfo>,
        type: TokenInfo.TokenType,
        tokenText: String,
        i: Int,
    ): Result<Int> {
        var j = i
        while (j < tokens.size) {
            val token = tokens[j].token
            if (token.type == type && token.text == tokenText) {
                return Result.success(j + 1 - i) // +1 because I want to include the last token, -i because I want to start from 0
            }
            j++
        }
        return Result.failure(Exception("Error: Missing $tokenText"))
    }

    fun getRangeOfTokenList(tokenList: List<TokenInfo>): Range {
        val startPos = tokenList[0].position.startIndex
        val endPos = tokenList[tokenList.size - 1].position.endIndex
        return Range(startPos, endPos)
    }

    fun getRangeofToken(token: TokenInfo): Range{
        val startPos = token.position.startIndex
        val endPos = token.position.endIndex
        return Range(startPos, endPos)
    }

    fun isOfTextAndType(
        token: TokenInfo.Token,
        s: String,
        operator: TokenInfo.TokenType,
    ): Boolean {
        return token.text == s && token.type == operator
    }
}
