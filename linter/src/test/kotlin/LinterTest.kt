import linterRules.CamelCaseRule
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LinterTest {
    @Test
    fun test001_testIsCamelCase() {
        val camelCaseRule = CamelCaseRule()
        assertTrue(camelCaseRule.isCamelCase("camelCase"))
        assertFalse(camelCaseRule.isCamelCase("camel_case"))
    }

}