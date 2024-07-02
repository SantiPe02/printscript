package formatter

import formater.configurationReader.JsonConfigurationReader
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
                "spaceBeforeColon": false,
                "spaceAfterColon": false,
                "spaceBeforeAndAfterEqual": false,
                "lineJumpBeforePrintln": 2,
                "indentation": 2
            }
            """.trimIndent()
        val configFile = File(tempDir, "config.json")
        configFile.writeText(jsonContent)

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(false, config.spaceBeforeColon)
        assertEquals(false, config.spaceAfterColon)
        assertEquals(false, config.spaceBeforeAndAfterEqual)
        assertEquals(2, config.lineJumpBeforePrintln)
        assertEquals(2, config.indentation)
    }

    @Test
    fun `test003 readFileAndBuildRules with missing values`() {
        val jsonContent =
            """
            {
                "spaceBeforeColon": false
            }
            """.trimIndent()
        val configFile = File(tempDir, "config.json")
        configFile.writeText(jsonContent)

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(false, config.spaceBeforeColon)
        assertEquals(true, config.spaceAfterColon)
        assertEquals(true, config.spaceBeforeAndAfterEqual)
        assertEquals(1, config.lineJumpBeforePrintln)
        assertEquals(4, config.indentation)
    }

    @Test
    fun `test004 readFileAndBuildRules with invalid file extension`() {
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText("{}")

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isFailure)
        assertEquals("The method was expecting a json file but got a yaml", result.exceptionOrNull()?.message)
    }

    @Test
    fun `test005 readFileAndBuildRules with invalid lineJumpBeforePrintln value`() {
        val jsonContent =
            """
            {
                "lineJumpBeforePrintln": 5
            }
            """.trimIndent()
        val configFile = File(tempDir, "config.json")
        configFile.writeText(jsonContent)

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(1, config.lineJumpBeforePrintln)
    }

    @Test
    fun `test006 readFileAndBuildRules with invalid indentation value`() {
        val jsonContent =
            """
            {
                "indentation": 10
            }
            """.trimIndent()
        val configFile = File(tempDir, "config.json")
        configFile.writeText(jsonContent)

        val result = JsonConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(4, config.indentation)
    }
}
