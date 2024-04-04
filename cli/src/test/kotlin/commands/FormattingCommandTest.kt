package commands

import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class FormattingCommandTest {
    @Test
    fun test001_whenFormattingCommandWithoutArgsThenCommandFailWithMessage() {
        val result = Formatting().test("")
        assertTrue(result.statusCode != 0)
        assertTrue(result.output.contains("Usage"))
    }

    @Test
    fun test002_whenFormattingCommandWithNotExistingFilesThenFailWithMessage() {
        val configFile = "configFile.json"
        val result = Formatting().test("file1.ps $configFile")
        assertTrue(result.statusCode != 0)
        assertEquals("the configuration file was not found in $configFile\n", result.output)
    }

    /**
     * since the FileFormatter search and modify all ps files is it does not find any of them no error is thrown.
     */
    @Test
    fun test003_whenFormattingCommandWithNotExistingSourcecodeFileThenRunSmooth() {
        val configFile = File.createTempFile("config", ".json")
        // since this json does not contain data the standard config is being use
        configFile.appendText("{\"one\":\"two\"}")

        val result = Formatting().test("file1.ps ${configFile.path.replace("\\", "/")}")
        assertTrue(result.statusCode == 0)
        configFile.deleteOnExit()
    }

    @Test
    fun test004_whenFormatMethodFailsThenAnFailWithMessage() {
        val configFile = File.createTempFile("config", ".txt")
        // since this json does not contain data the standard config is being use
        configFile.appendText("{\"one\":\"two\"}")

        val result = Formatting().test("file1.ps ${configFile.path.replace("\\", "/")}")
        assertTrue(result.statusCode != 0)
        assertEquals("No class found for that extension\n", result.output)
        configFile.deleteOnExit()
    }
}
