package formater

/**
 * If several rules with the same pattern it will only use the first rule
 */
class RegexFormatter(val rules: List<Rule> = listOf()) : IFormatter {
    override fun formatString(sourceCode: String): String {
        return mandatoryFormatting(formatByRules(sourceCode))
    }

    private fun formatByRules(sourceCode: String): String =
        sourceCode.replace(Regex(buildCombinedPatter(rules))) {
            findMatchingRule(it, rules)?.newText ?: it.value
        }

    private fun buildCombinedPatter(rules: List<Rule>): String = rules.joinToString(separator = "|") { it.pattern }

    private fun mandatoryFormatting(sourceCode: String): String =
        sourceCode.replace(Regex("\\s*([-+*/])\\s*| {2,}|;(?!\n)(?!$)")) {
            if (it.groups[1] != null) {
                " ${it.value.trim()} "
            } else if (it.value == ";") {
                ";\n"
            } else {
                " "
            }
        }

    private fun findMatchingRule(
        matchResult: MatchResult,
        ruleList: List<Rule>,
    ): Rule? {
        for (rule in ruleList) {
            if (matchResult.groupValues.any { it.isNotEmpty() && rule.pattern.toRegex().matches(it) }) {
                return rule
            }
        }
        return null
    }

    data class Rule(val pattern: String, val newText: String)
}
