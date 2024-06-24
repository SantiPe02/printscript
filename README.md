Usage: printScript [<options>] <version> <command> [<args>]...

The CLI arguments consists of three parts:

* The printscript version (1.0 or 1.1)
* The command to execute and
* The WHOLE path (or paths) of the file to be processed.

For example
* ./gradlew run --args="1.1 validate ../printscript/code.ps"

There are for commands:

1. validate - Validates that the AST is correctly generated
* Example: ./gradlew run --args="1.1 validate ../printscript/code.ps"
2. formatting - Formats the code, editing the file by applying the rules
* Example: ./gradlew run --args="1.1 formatting ../printscript/code.ps ../printscript/formatterConfig.json"
3. analyzing - Analyzes the code using the linter and returns the rules that are being broken
* Example: ./gradlew run --args="1.1 analyzing ../printscript/code.ps ../printscript/linterConfig.json"
4. execute - Interprets the code
* Example: ./gradlew run --args="1.1 execute ../printscript/code.ps"