package formatter

import formater.format
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class FormatTest {
    @Test
    fun `test format`() {
        val testFile = "src/test/resources/formatFileTest.txt"
        val configFile = File("src/test/resources/config.json")

        val result = format(testFile, configFile)

        assertTrue(result.isSuccess, "Format function failed")
    }
}
