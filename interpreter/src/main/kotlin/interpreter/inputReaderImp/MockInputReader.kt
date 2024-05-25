package interpreter.inputReaderImp

import interpreter.InputReadResult
import interpreter.InputReader

class MockInputReader(val mockInput: List<String>) : InputReader {
    override fun readInput(): InputReadResult {
        val input = if (mockInput.size > 0) mockInput.first() else ""
        return InputReadResult(
            input,
            MockInputReader(
                if (mockInput.size > 1) mockInput.subList(1, mockInput.size) else listOf(),
            ),
        )
    }
}
