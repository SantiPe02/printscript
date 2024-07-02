import ast.Range
import org.junit.jupiter.api.Test
import result.validation.ValidResult
import result.validation.WarningResult

class ResultTest {
    @Test
    fun `test001 valid result Equals function`() {
        val validResult1 = ValidResult()
        val validResult2 = ValidResult()
        val wariningResult = WarningResult(Range(1, 2), "message")
        assert(validResult1.equals(validResult2))
        assert(!validResult1.equals(wariningResult))
    }

    @Test
    fun `test002 valid result HashCode function`() {
        val validResult = ValidResult()
        assert(validResult.hashCode() == validResult.javaClass.hashCode())
    }

    @Test
    fun `test003 warning result Equals function`() {
        val warningResult1 = WarningResult(Range(1, 2), "message")
        val warningResult2 = WarningResult(Range(1, 2), "message")
        val validResult = ValidResult()
        assert(warningResult1.equals(warningResult2))
        assert(!warningResult1.equals(validResult))
    }

    @Test
    fun `test004 warning result HashCode function`() {
        val warningResult = WarningResult(Range(1, 2), "message")
        assert(warningResult.hashCode() == 31 * warningResult.range.hashCode() + warningResult.message.hashCode())
    }
}
