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
        if(tokens[++j].token.text != "(") return Result.failure(Exception("Expected parentheses after if " + commons.getRangeofToken(tokens[j])))

        var conditions: Collection<Condition> = emptyList()
        var scope: Scope = Scope("", Range(0,0), emptyList())
        var closingBracket: Int = 0

        getConditions(tokens, j).onFailure { return Result.failure(it) }
            .onSuccess {
                conditions = it
                j = nextIndexFromClosingChar(tokens, "(", j)
            }

        declareScope(tokens, j).onFailure { return Result.failure(it) }.onSuccess { scope = it }
        commons.searchForClosingCharacter(tokens, "{", j).onFailure { return Result.failure(it) }
            .onSuccess { closingBracket = it }


        if(followUpConditionExists(tokens, closingBracket + 1))
            declareIfAndElse(tokens, closingBracket + 1, i, conditions, scope)

        val range = commons.getRangeOfTokenList(listOf(tokens[i], tokens[closingBracket]))
        return Result.success(IfStatement(range, conditions, scope))
    }

    // returns either the next index, or, if there is no closing character, the current index +1
    private fun nextIndexFromClosingChar(tokens: List<TokenInfo>, char: String, i: Int): Int
    {
       return (commons.searchForClosingCharacter(tokens, char, i).getOrNull() ?: i) + 1
    }

    private fun declareIfAndElse(tokens: List<TokenInfo>, start:Int, i: Int, conditions: Collection<Condition>, scope: Scope): Result<IfAndElseStatement> {
        var j = i
        var elseScope: Scope = Scope("", Range(0,0), emptyList())
        var closingBracket: Int = 0

        if(tokens[++j].token.text != "{") return Result.failure(Exception("Expected brackets after \"else\" statement " + commons.getRangeofToken(tokens[j])))
        declareScope(tokens, j).onFailure { return Result.failure(it) }.onSuccess { elseScope = it }
        commons.searchForClosingCharacter(tokens, "{", j).onFailure { return Result.failure(it) }
            .onSuccess { closingBracket = it }

        val range = commons.getRangeOfTokenList(listOf(tokens[start], tokens[closingBracket])) // Está mal, arranca desde el else, debería arrancar desde el if
        return Result.success(IfAndElseStatement(range, conditions, scope, elseScope))
    }


    private fun followUpConditionExists(tokens: List<TokenInfo>, i: Int): Boolean {
        if(tokens.size <= i) return false
        return when(tokens[i].token.text){
            "else" -> true
            /*"else if" -> true     as for now, we only handle "else"*/
            else -> false
        }
    }

    private fun declareScope(tokens: List<TokenInfo>, i: Int): Result<Scope> {
        val scope = getScope(tokens, i)
        scope.onSuccess {
            return Result.success(it)
        }
            .onFailure { return Result.failure(it)}
        return Result.failure(Exception("Unknown error at " + commons.getRangeofToken(tokens[i])))
    }

    private fun getScope(tokens: List<TokenInfo>, i: Int): Result<Scope>{
        var j  = i
        if(tokens[++j].token.text != "{") return Result.failure(Exception("Expected brackets after closing parentheses " + commons.getRangeofToken(tokens[j])))

        val closingBracket = commons.searchForClosingCharacter(tokens, "{", j)

        closingBracket.onSuccess {
            val scopeTokens = tokens.subList(j, it)
            return MyParser().parseTokens(scopeTokens)
        }

        return Result.failure(Exception(closingBracket.exceptionOrNull()?.message ?: "Unknown error"))
    }

    private fun getConditions(tokens: List<TokenInfo>, i: Int): Result<Collection<Condition>> {
        val amountCond = getConditionsSize(tokens, i)

        amountCond.onSuccess {
            return if(it == 1) {
                if(tokens[i + 1].token.type == TokenInfo.TokenType.IDENTIFIER){
                    val range = commons.getRangeOfTokenList(listOf(tokens[i + 1]))
                    val newCondition = BooleanCondition(range, VariableArgument(range, tokens[i + 1].token.text))
                    Result.success(mutableListOf(newCondition))
                } else
                    Result.failure(Exception("Boolean condition should be of identifier type " + commons.getRangeofToken(tokens[i+1])))
            } else{
                Result.failure(Exception("TODO: Parser cannot handle binary conditions yet " + commons.getRangeofToken(tokens[i+1])))
            }
        }

        return Result.failure(Exception(amountCond.exceptionOrNull()?.message ?: "Unknown error"))
    }

    private fun getConditionsSize(tokens: List<TokenInfo>, i: Int): Result<Int> {
        var j = i
        var amount = 0
        val exists = commons.searchForClosingCharacter(tokens,  "(", i)
        if(exists.isFailure) return Result.failure(Exception("Invalid syntax: missing closing parentheses " + commons.getRangeofToken(tokens[i])))

        while(tokens[++j].token.text != ")") {
            amount++
        }
        return Result.success(amount)
    }

}