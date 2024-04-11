package util

const val STRING_SEPARATOR_PATTERN = """("(?:\\.|[^"\\])*"|==|<=|>=|!=|\d+\.\d+|\w+|[^\w\s])"""
const val KEY_WORD_PATTERN =
    "let|string|number|if|else|for|while|fun|return|class|interface|object|when|try|catch|throw|throws|finally|break|" +
        "continue|this|super|package|import|as|in|is|new|typeof|instanceof|void|var|val|const|static|public|private|protected|final|abstract|native|synchronized|transient|volatile|strictfp|default|extends|implements|boolean|char"
const val LITERAL_PATTERN = "\\d+\\.\\d+|\\d+|(\"(?:\\\\\"|[^\"])*\")|'.?'|true|false"
const val OPERATOR_PATTERN = "[+\\-*/><=]+|<=|>=|==|!="
const val SPECIAL_SYMBOL_PATTERN = "[;:(){}]"
