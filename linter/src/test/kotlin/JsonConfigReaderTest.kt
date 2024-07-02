package configurationReader

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.api.io.TempDir
import java.io.File

class JsonConfigReaderTest {
    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test001 getFileExtension`() {
        assertEquals("json", JsonConfigurationReader.getFileExtension())
    }

    @Test
    fun `test002 readFileAndBuildRules with valid json file`() {
        val jsonContent =
            """
            {
                "camelCase": false,
                "printlnWithoutExpression": false,
                "undeclaredVariableStatement": false,
                "readInputWithoutExpression": false
            }
            """.trimIndent()
        val configFile = File(tempDir, "config.json")
        configFile.writeText(jsonContent)

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(false, config.camelCase)
        assertEquals(false, config.printlnWithoutExpression)
        assertEquals(false, config.undeclaredVariableStatement)
        assertEquals(false, config.readInputWithoutExpression)
    }

    @Test
    fun `test003 readFileAndBuildRules with missing values`() {
        val jsonContent =
            """
            {
                "camelCase": false
            }
            """.trimIndent()
        val configFile = File(tempDir, "config.json")
        configFile.writeText(jsonContent)

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(false, config.camelCase)
        assertEquals(true, config.printlnWithoutExpression)
        assertEquals(true, config.undeclaredVariableStatement)
        assertEquals(true, config.readInputWithoutExpression)
    }

    @Test
    fun `test004 readFileAndBuildRules with invalid file extension`() {
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText("{}")

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isFailure)
        assertEquals("The method was expecting a json file but got a yaml", result.exceptionOrNull()?.message)
    }
}
