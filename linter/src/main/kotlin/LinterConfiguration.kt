

data class LinterConfiguration(
    val camelCase: Boolean = true,
    val printlnWithoutExpression: Boolean = true,
    val undeclaredVariableStatement: Boolean = true,
    val readInputWithoutExpression: Boolean = true,
)
