package stringReader

import lexer.Context
import lexer.Lexer
import token.TokenInfo

class PartialStringReadingLexer(
    val lexer: Lexer,
    val notProcessed: String = "",
    val context: Context = Context(0),
) {
    fun tokenizeString(text: String): Pair<PartialStringReadingLexer, List<TokenInfo>> {
        var notProcessedAux = ""
        var toProcessText = notProcessed + text
        val auxContext: Context

        val lastColonIndex = toProcessText.lastIndexOf(';')
        if (lastColonIndex != -1 && lastColonIndex != toProcessText.length - 1) {
            notProcessedAux = toProcessText.substring(lastColonIndex + 1)
            toProcessText = toProcessText.substring(0, lastColonIndex)
            auxContext = Context(context.startingLength + toProcessText.length + 1)
        } else if (lastColonIndex == -1) {
            notProcessedAux = toProcessText
            toProcessText = ""
            auxContext = Context(context.startingLength)
        } else {
            auxContext = Context(context.startingLength + toProcessText.length + 1)
        }

        return Pair(
            PartialStringReadingLexer(
                lexer,
                notProcessedAux,
                auxContext,
            ),
            lexer.tokenize(toProcessText, context),
        )
    }
}
