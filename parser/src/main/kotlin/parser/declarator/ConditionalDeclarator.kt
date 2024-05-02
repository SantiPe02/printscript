package parser.declarator
import ast.AST
import ast.BooleanCondition
import ast.Condition
import ast.Conditional
import ast.IfAndElseStatement
import ast.IfStatement
import ast.Range
import ast.Scope
import ast.VariableArgument
import parser.MyParser
import parser.ParserCommons
import token.TokenInfo

class ConditionalDeclarator {
    private val commons = ParserCommons()

    fun declareIf(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Conditional> {
        var j = i
        if (tokens[++j].token.text != "(") return expectedCharacterError("(", tokens[j])

        var conditions: Collection<Condition> = emptyList()
        var scope = emptyScope(commons.getRangeOfToken(tokens[i]))
        var closingBracket = 0

        getConditions(tokens, j)
            .onFailure { return Result.failure(it) }
            .onSuccess {
                conditions = it
                j = nextIndexFromClosingChar(tokens, "(", j)
            }

        declareScope(tokens, j)
            .onFailure { return Result.failure(it) }
            .onSuccess { scope = it }

        commons.searchForClosingCharacter(tokens, "{", j)
            .onFailure { return Result.failure(it) }
            .onSuccess { closingBracket = it }

        if (followUpConditionExists(tokens, closingBracket + 1)) {
            return declareIfAndElse(tokens, i, closingBracket + 1, conditions, scope)
        }

        return declareSuccessIf(tokens, i, conditions, scope, closingBracket)
    }

    private fun declareIfAndElse(
        tokens: List<TokenInfo>,
        start: Int,
        i: Int,
        conditions: Collection<Condition>,
        scope: Scope,
    ): Result<IfAndElseStatement> {
        var j = i + 1
        if (tokens[j].token.text != "{") {
            return expectedCharacterError("{", tokens[j])
        }

        var elseScope = emptyScope(commons.getRangeOfToken(tokens[j]))
        var closingBracket = 0

        declareScope(tokens, j)
            .onFailure { return Result.failure(it) }
            .onSuccess { elseScope = it }

        commons.searchForClosingCharacter(tokens, "{", j)
            .onFailure { return Result.failure(it) }
            .onSuccess { closingBracket = it }
        return declareSuccessIfAndElse(tokens, start, conditions, scope, elseScope, closingBracket)
    }

    private fun declareSuccessIf(
        tokens: List<TokenInfo>,
        i: Int,
        conditions: Collection<Condition>,
        scope: Scope,
        closingBracket: Int,
    ): Result<IfStatement> {
        val range = commons.getRangeOfTokenList(listOf(tokens[i], tokens[closingBracket]))

        return Result.success(IfStatement(range, conditions, scope))
    }

    // returns either the next index, or, if there is no closing character, the current index +1
    private fun nextIndexFromClosingChar(
        tokens: List<TokenInfo>,
        char: String,
        i: Int,
    ): Int {
        return (commons.searchForClosingCharacter(tokens, char, i).getOrNull() ?: i) + 1
    }

    private fun declareSuccessIfAndElse(
        tokens: List<TokenInfo>,
        i: Int,
        conditions: Collection<Condition>,
        scope: Scope,
        elseScope: Scope,
        closingBracket: Int,
    ): Result<IfAndElseStatement> {
        val range = commons.getRangeOfTokenList(listOf(tokens[i], tokens[closingBracket]))
        return Result.success(IfAndElseStatement(range, conditions, scope, elseScope))
    }

    private fun followUpConditionExists(
        tokens: List<TokenInfo>,
        i: Int,
    ): Boolean {
        if (tokens.size <= i) return false
        return when (tokens[i].token.text) {
            "else" -> true
            // "else if" -> true     as for now, we only handle "else"
            else -> false
        }
    }

    private fun declareScope(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Scope> {
        val scope = getScope(tokens, i)
        scope
            .onSuccess { return Result.success(it) }
            .onFailure { return Result.failure(it) }

        return Result.failure(Exception("Unknown error. " + commons.getRangeOfTokenAsString(tokens[i])))
    }

    private fun getScope(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Scope> {
        if (tokens[i].token.text != "{") return expectedCharacterError("{", tokens[i])

        val closingBracket = commons.searchForClosingCharacter(tokens, "{", i)

        closingBracket.onSuccess {
            if (numsAreSuccessive(i, it)) return Result.success(emptyScope(commons.getRangeOfToken(tokens[i])))
            val scopeTokens = tokens.subList(i + 1, it)
            return MyParser().parseTokens(scopeTokens)
        }

        return Result.failure(Exception(closingBracket.exceptionOrNull()?.message ?: "Unknown error"))
    }

    private fun numsAreSuccessive(
        i: Int,
        j: Int,
    ): Boolean {
        return (i == j + 1) || (i + 1 == j)
    }

    // Todo: Eventually give the scope name
    private fun emptyScope(range: Range): Scope {
        return Scope("program", range, emptyList())
    }

    private fun getConditions(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Collection<Condition>> {
        val amountCond = getConditionsSize(tokens, i)

        amountCond.onSuccess {
            getValidConditions(it, tokens, i)
                .onSuccess { return Result.success(it) }
                .onFailure { return Result.failure(it) }
        }

        return Result.failure(Exception(amountCond.exceptionOrNull()?.message ?: "Unknown error"))
    }

    private fun getValidConditions(
        amount: Int,
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Collection<Condition>> {
        return if (amount == 1) {
            getBooleanCondition(tokens, i + 1)
        } else {
            handleNormalError("TODO: Parser cannot handle binary conditions yet", tokens[i + 1])
        }
    }

    private fun getBooleanCondition(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Collection<Condition>> {
        if (tokens[i].token.type == TokenInfo.TokenType.IDENTIFIER) {
            val range = commons.getRangeOfTokenList(listOf(tokens[i]))
            val newCondition = BooleanCondition(range, VariableArgument(range, tokens[i].token.text))
            return Result.success(mutableListOf(newCondition))
        }

        return handleNormalError("Boolean condition should be of identifier type", tokens[i])
    }

    private fun getConditionsSize(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Int> {
        var j = i
        var amount = 0
        val exists = commons.searchForClosingCharacter(tokens, "(", i)
        if (exists.isFailure) {
            return Result.failure(
                Exception("Invalid syntax: missing closing parentheses. " + commons.getRangeOfTokenAsString(tokens[i])),
            )
        }

        while (tokens[++j].token.text != ")") {
            amount++
        }
        return Result.success(amount)
    }

    private fun <T : AST> expectedCharacterError(
        char: String,
        token: TokenInfo,
    ): Result<T> {
        return Result.failure(Exception("Expected \"$char\": " + commons.getRangeOfTokenAsString(token)))
    }

    private fun <T : AST> handleNormalError(
        string: String,
        token: TokenInfo,
    ): Result<T> {
        return Result.failure(Exception(string + " at: " + commons.getRangeOfTokenAsString(token)))
    }
}
