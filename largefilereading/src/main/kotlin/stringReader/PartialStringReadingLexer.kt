package stringReader

import lexer.Context
import lexer.Lexer
import token.TokenInfo

// para leer archivos grandes. Lee de a pedazos. Es literalmente un Lexer

// recib(v?)e el lexer en sí y lo extiende

// Handleea archivos grandes.
class PartialStringReadingLexer(
    val lexer: Lexer,
    val notProcessed: String = "",
    val context: Context = Context(0), // puntero
) {
    fun tokenizeString(
        text: String,
        isLast: Boolean,
    ): Pair<PartialStringReadingLexer, List<TokenInfo>> {
        var notProcessedAux = ""
        var toProcessText = notProcessed + text // lo q se tiene q procesar en esta iteración.
        val auxContext: Context

        val lastColonIndex = toProcessText.lastIndexOf(';')
        val lastBracketIndex = toProcessText.lastIndexOf('}')
        val lastIndex = maxOf(lastColonIndex, lastBracketIndex)

        // lo q se tiene q procesar es desde el 0 hasta el último punto y coma o llave cerrada.
        if (lastIndex != -1 && lastIndex != toProcessText.length - 1) {
            notProcessedAux = toProcessText.substring(lastIndex + 1)
            toProcessText = toProcessText.substring(0, lastIndex + 1)
            auxContext = Context(context.startingLength + toProcessText.length + 1)
        } else if (lastIndex == -1 && isLast) {
            // didn't find any last index, but is the last iteration, so it processes it nonetheless
            auxContext = Context(context.startingLength + toProcessText.length + 1)
        } else if (lastIndex == -1) {
            // if last index does not exist or not found (entonces guardamos to-do)
            notProcessedAux = toProcessText // se guarda la parte que quedó sin procesar. Para poder procesarla en el próximo llamado.
            toProcessText = ""
            auxContext = Context(context.startingLength)
        } else {
            // se encontró un lastIndex y ese es el último token.
            auxContext = Context(context.startingLength + toProcessText.length + 1)
        }

        return Pair(
            PartialStringReadingLexer(
                lexer,
                notProcessedAux, // manda la parte que no se procesó
                auxContext, // el index de la úlitma posición q se procesó.
            ),
            lexer.tokenize(toProcessText, context),
        )
    }
}
