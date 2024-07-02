package configurationReader

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
            camelCase: false
            printlnWithoutException: false
            undeclaredVariableStatement: false
            readInputWithoutExpression: false
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(false, config.camelCase)
        assertEquals(false, config.printlnWithoutExpression)
        assertEquals(false, config.undeclaredVariableStatement)
        assertEquals(false, config.readInputWithoutExpression)
    }

    @Test
    fun `test readFileAndBuildRules with missing values`() {
        val yamlContent =
            """
            camelCase: false
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(false, config.camelCase)
        assertEquals(true, config.printlnWithoutExpression)
        assertEquals(true, config.undeclaredVariableStatement)
        assertEquals(true, config.readInputWithoutExpression)
    }

    @Test
    fun `test readFileAndBuildRules with invalid file extension`() {
        val configFile = File(tempDir, "config.json")
        configFile.writeText("{}")

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isFailure)
        assertEquals("The method was expecting a yaml file but got a json", result.exceptionOrNull()?.message)
    }

    @Test
    fun `test readFileAndBuildRules with invalid camelCase value`() {
        val yamlContent =
            """
            camelCase: "invalid"
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(true, config.camelCase)
    }

    @Test
    fun `test readFileAndBuildRules with invalid printlnWithoutException value`() {
        val yamlContent =
            """
            printlnWithoutException: "invalid"
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(true, config.printlnWithoutExpression)
    }

    @Test
    fun `test readFileAndBuildRules with invalid undeclaredVariableStatement value`() {
        val yamlContent =
            """
            undeclaredVariableStatement: "invalid"
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(true, config.undeclaredVariableStatement)
    }

    @Test
    fun `test readFileAndBuildRules with invalid readInputWithoutExpression value`() {
        val yamlContent =
            """
            readInputWithoutExpression: "invalid"
            """.trimIndent()
        val configFile = File(tempDir, "config.yaml")
        configFile.writeText(yamlContent)

        val result = YamlConfigurationReader.readFileAndBuildRules(configFile)

        assertTrue(result.isSuccess)
        val config = result.getOrNull() ?: fail("Expected a valid LinterConfiguration")

        assertEquals(true, config.readInputWithoutExpression)
    }
}
