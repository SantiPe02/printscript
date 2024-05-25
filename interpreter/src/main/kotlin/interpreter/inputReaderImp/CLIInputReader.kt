package interpreter.inputReaderImp

import interpreter.InputReadResult
import interpreter.InputReader

class CLIInputReader : InputReader {
    override fun readInput(): InputReadResult {
        return InputReadResult(readln(), this)
    }
}
