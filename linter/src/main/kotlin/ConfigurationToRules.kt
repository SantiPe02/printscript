
import linterRules.CamelCaseRule
import linterRules.LinterRule
import linterRules.PrintlnWithoutExpressionRule

public fun translateFormatterConfigurationToRules(config: LinterConfiguration): List<LinterRule> {
    val rules = mutableListOf<LinterRule>()

    if (config.camelCase) {
        rules += CamelCaseRule()
    }
    if (config.printlnWithoutExpression) {
        rules += PrintlnWithoutExpressionRule()
    }

    return rules
}
