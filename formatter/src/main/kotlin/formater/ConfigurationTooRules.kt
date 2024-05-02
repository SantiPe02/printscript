package formater

public fun translateFormatterConfigurationToRules(config: FormatterConfiguration): List<RegexFormatter.Rule> {
    var rules = listOf<RegexFormatter.Rule>()

    rules = colonRule(config.spaceBeforeColon, config.spaceAfterColon, rules)
    rules +=
        if (config.spaceBeforeAndAfterEqual) {
            RegexFormatter.Rule("\\s*=\\s*", " = ")
        } else {
            RegexFormatter.Rule("\\s*=\\s*", "=")
        }

    var println = ""
    for (i in 0..config.lineJumpBeforePrintln)
        println += "\n"
    println += "println"
    return rules + RegexFormatter.Rule("\n{${config.lineJumpBeforePrintln + 1},}println", println)
}

private fun colonRule(
    spaceBeforeColon: Boolean,
    spaceAfterColon: Boolean,
    list: List<RegexFormatter.Rule>,
): List<RegexFormatter.Rule> {
    return list +
        if (spaceBeforeColon && spaceAfterColon) {
            RegexFormatter.Rule("\\s*:\\s*", " : ")
        } else if (!spaceBeforeColon && !spaceAfterColon) {
            RegexFormatter.Rule("\\s*:\\s*", ":")
        } else if (spaceBeforeColon) {
            RegexFormatter.Rule("\\s*:\\s*", " :")
        } else {
            RegexFormatter.Rule("\\s*:\\s*", ": ")
        }
}
