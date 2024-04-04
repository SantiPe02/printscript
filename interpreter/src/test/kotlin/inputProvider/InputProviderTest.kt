package inputProvider

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InputProviderTest {
    @Test
    fun testFileReader() {
        val reader = FileInput("..\\code.txt").getContent()
        assertEquals("let a: number = 9;", reader)
    }
}
