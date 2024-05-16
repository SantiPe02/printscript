package interpreter.inputReaderImp

import interpreter.InputReadResult
import interpreter.InputReader

class MockInputReader(val mockInput: List<String>) : InputReader {
    override fun readInput(): InputReadResult {
        val input = mockInput.first()
        return InputReadResult(input, MockInputReader(mockInput.subList(1, mockInput.size - 1)))
    }
}
