package parser.declarator

import ast.*
import parser.MyParser
import parser.ParserCommons
import token.TokenInfo

class ConditionalDeclarator {
    val commons = ParserCommons()
    fun declareIf(
        tokens: List<TokenInfo>,
        i: Int,
    ): Result<Conditional> {
        var j = i
        if(tokens[++j].token.text != "(") return Result.failure(Exception("Expected parentheses after if"))

        val conditions = getConditions(tokens, j)
        conditions.onSuccess {conds ->
            val scope = declareScope(tokens, j).onSuccess { scope ->
                val closingBracket = commons.searchForClosingCharacter(tokens, "{", j).onSuccess { index ->
                    if(followUpConditionExists(tokens, index + 1))
                        handleFollowUpCondition(tokens, index + 1)

                    val range = commons.getRangeOfTokenList(listOf(tokens[i], tokens[index]))
                    return Result.success(IfStatement(range, conds, scope))
                }
                return Result.failure(Exception(closingBracket.exceptionOrNull()?.message ?: "Unknown error"))
            }
            return Result.failure(Exception(scope.exceptionOrNull()?.message ?: "Unknown error"))
        }
        return Result.failure(Exception(conditions.exceptionOrNull()?.message ?: "Unknown error"))
    }


    fun handleFollowUpCondition(tokens: List<TokenInfo>, i: Int): Result<Conditional> {
        return when(tokens[i].token.text){
            "else" -> declareElse(tokens, i)
            else -> Result.failure(Exception("Unknown error"))
        }
    }

    fun followUpConditionExists(tokens: List<TokenInfo>, i: Int): Boolean {
        if(tokens.size <= i) return false
        return when(tokens[i].token.text){
            "else" -> true
            /*"else if" -> true     as for now, we only handle "else"*/
            else -> false
        }
    }

    fun declareScope(tokens: List<TokenInfo>, i: Int): Result<Scope> {
        val closingIndex = commons.searchForClosingCharacter(tokens, "(", i)
        closingIndex.onSuccess {
            val scope = getScope(tokens, it)
            scope.onSuccess {
                return Result.success(it)
            }
                .onFailure { return Result.failure(it)}
        }
        return Result.failure(Exception(closingIndex.exceptionOrNull()?.message ?: "Unknown error"))
    }

    fun getScope(tokens: List<TokenInfo>, i: Int): Result<Scope>{
        var j  = i
        if(tokens[++j].token.text != "{") return Result.failure(Exception("Expected brackets after closing parentheses"))

        val closingBracket = commons.searchForClosingCharacter(tokens, "{", j)

        closingBracket.onSuccess {
            val scopeTokens = tokens.subList(j, it)
            return MyParser().parseTokens(scopeTokens)
        }

        return Result.failure(Exception(closingBracket.exceptionOrNull()?.message ?: "Unknown error"))
    }

    fun getConditions(tokens: List<TokenInfo>, i: Int): Result<Collection<Condition>> {
        val amountCond = getConditionsSize(tokens, i)

        amountCond.onSuccess {
            return if(it == 1) {
                if(tokens[i + 1].token.type == TokenInfo.TokenType.IDENTIFIER){
                    val range = commons.getRangeOfTokenList(listOf(tokens[i + 1]))
                    val newCondition = BooleanCondition(range, VariableArgument(range, tokens[i + 1].token.text))
                    Result.success(mutableListOf(newCondition))
                } else
                    Result.failure(Exception("Boolean condition should be of identifier type"))
            } else{
                Result.failure(Exception("TODO: Parser cannot handle binary conditions yet"))
            }
        }

        return Result.failure(Exception(amountCond.exceptionOrNull()?.message ?: "Unknown error"))
    }

    fun getConditionsSize(tokens: List<TokenInfo>, i: Int): Result<Int> {
        var j = i
        var amount = 0
        val exists = commons.searchForClosingCharacter(tokens,  "(", i)
        if(exists.isFailure) return Result.failure(Exception("Invalid syntax: missing closing parentheses"))

        while(tokens[++j].token.text != ")") {
            amount++
        }
        return Result.success(amount)
    }

}