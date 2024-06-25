# How to use the CLI

Usage: `printScript [<options>] <command> [<args>]...`

The CLI arguments consists of two parts: 
1. The command to execute and
2. The <u>WHOLE</u> path to the file to be processed.

For example
```bash
./gradlew run --args="validate ../printscript/code.ps"
```

There are for commands:
1. `validate` - Validates that the AST is correctly generated
   * Example: `./gradlew run --args="validate ../printscript/code.ps"`
2. `formatting` - Formats the code, editing the file by applying the rules
   * Example: `./gradlew run --args="formatting ../printscript/code.ps ../printscript/formatterConfig.json"` 
3. `analyzing` - Analyzes the code using the linter and returns the rules that are being broken
    * Example: `./gradlew run --args="analyzing ../printscript/code.ps ../printscript/linterConfig.json"`
4. `execute` - Interprets the code
    * Example: `./gradlew run --args="execute ../printscript/code.ps"`
