
import linterRules.CamelCaseRule
import linterRules.LinterRule
import linterRules.PrintlnWithoutExpressionRule
import linterRules.ReadInputWithoutExpressionRule

public fun translateFormatterConfigurationToRules(config: LinterConfiguration): List<LinterRule> {
    val rules = mutableListOf<LinterRule>()

    if (config.camelCase) {
        rules += CamelCaseRule()
    }
    if (config.printlnWithoutExpression) {
        rules += PrintlnWithoutExpressionRule()
    }
    if (config.readInputWithoutExpression) {
        rules += ReadInputWithoutExpressionRule()
    }

    return rules
}
