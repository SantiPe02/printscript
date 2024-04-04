package result.validation

import ast.Range

class WarningResult(val range: Range, val message: String): ValidationResult {
    fun getWarning(): Pair<Range, String> = Pair(range, message)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WarningResult) return false

        if (range != other.range) return false
        if (message != other.message) return false

        return true
    }
}