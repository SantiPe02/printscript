package util

const val STRING_SEPARATOR_PATTERN = """("(?:\\.|[^"\\])*"|==|<=|>=|!=|\d+\.\d+|\w+|[^\w\s])"""
const val KEY_WORD_PATTERN_V11 = "let|string|number|if|else|const|boolean|return|object"
const val LITERAL_PATTERN = "\\d+\\.\\d+|\\d+|(\"(?:\\\\\"|[^\"])*\")|'.?'|true|false"
const val OPERATOR_PATTERN = "[+\\-*/><=]+|<=|>=|==|!="
const val SPECIAL_SYMBOL_PATTERN = "[;:(){}]"
const val KEY_WORD_PATTERN_V10 = "let|string|number|boolean|return|object"
