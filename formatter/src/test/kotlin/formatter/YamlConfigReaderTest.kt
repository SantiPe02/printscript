package formatter

import formater.configurationReader.YamlConfigurationReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.api.io.TempDir
import java.io.File

class YamlConfigReaderTest {
    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test getFileExtension`() {
        assertEquals("yaml", YamlConfigurationReader.getFileExtension())
    }

    @Test
    fun `test readFileAndBuildRules with valid yaml file`() {
        val yamlContent =
            """
            spaceBeforeColon: false
            spaceAfterColon: false
            spaceBeforeAndAfterEqual: false
            lineJumpBeforePrintln: 2
            indentation: 2
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(false, config.spaceBeforeColon)
        assertEquals(false, config.spaceAfterColon)
        assertEquals(false, config.spaceBeforeAndAfterEqual)
        assertEquals(2, config.lineJumpBeforePrintln)
        assertEquals(2, config.indentation)
    }

    @Test
    fun `test readFileAndBuildRules with missing values`() {
        val yamlContent =
            """
            spaceBeforeColon: false
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(false, config.spaceBeforeColon)
        assertEquals(true, config.spaceAfterColon)
        assertEquals(true, config.spaceBeforeAndAfterEqual)
        assertEquals(1, config.lineJumpBeforePrintln)
        assertEquals(4, config.indentation)
    }

    @Test
    fun `test readFileAndBuildRules with invalid file extension`() {
        val configFile = File(tempDir, "config.json")
        configFile.writeText("{}")

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isFailure)
        assertEquals("The method was expecting a json file but got a json", result.exceptionOrNull()?.message)
    }

    @Test
    fun `test readFileAndBuildRules with invalid lineJumpBeforePrintln value`() {
        val yamlContent =
            """
            lineJumpBeforePrintln: 5
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(1, config.lineJumpBeforePrintln)
    }

    @Test
    fun `test readFileAndBuildRules with invalid indentation value`() {
        val yamlContent =
            """
            indentation: 10
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid FormatterConfiguration")

        assertEquals(4, config.indentation)
    }
}
