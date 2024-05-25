package interpreter

interface InputReader {
    fun readInput(): InputReadResult
}

data class InputReadResult(val input: String, val reader: InputReader)
