package formater

import util.operatorPattern


class Formatter: IFormatter {
    override fun formatString(sourceCode: String): String {
        return sourceCode.replace(Regex("\\s*($operatorPattern)\\s*| {2,}|;(?!\n)(?!$)")) {
            if (it.groups[1] != null) " ${it.value.trim()} " // Add spaces around operators
            else if (it.value == ";") ";\n" // Replace semicolons with semicolon followed by newline
            else " " // Replace consecutive spaces with a single space
        }


        /**
        var newText = Regex(operatorPattern).replace(sourceCode," $0 ")
        newText = Regex(" {2,}").replace(newText, " ")
        return Regex(";(?!\n)(?!$)").replace(newText, ";\n")
        **/
    }
}