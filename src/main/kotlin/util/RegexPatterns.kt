package util

const val stringSeparatorPattern = """("(?:\\.|[^"\\])*"|==|<=|>=|!=|\w+|[^\w\s])""";
const val keywordPattern = "let|string|number|if|else|for|while|fun|return|class|interface|object|when|try|catch|throw|throws|finally|break|continue|this|super|package|import|as|in|is|new|typeof|instanceof|void|var|val|const|static|public|private|protected|final|abstract|native|synchronized|transient|volatile|strictfp|default|extends|implements|boolean|char|byte|short|int|long|float|double";
const val literalPattern = "\\d+|(\"(?:\\\\\"|[^\"])*\")|'.?'|true|false";
const val operatorPattern = "[+\\-*/><=]+|<=|>=|==|!=";
const val specialSymbolPattern = "[;:(){}]";