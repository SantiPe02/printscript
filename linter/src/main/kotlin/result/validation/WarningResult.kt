package result.validation

import ast.Range

class WarningResult(val range: Range, val message: String): ValidationResult {
    fun getWarning(): Pair<Range, String> = Pair(range, message)
}