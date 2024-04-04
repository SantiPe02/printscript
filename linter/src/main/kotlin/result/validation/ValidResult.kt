package result.validation

class ValidResult: ValidationResult {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ValidResult) return false
        return true
    }
}