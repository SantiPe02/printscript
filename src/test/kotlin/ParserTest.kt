import ast.*
import lexer.LexerImpl
import org.junit.jupiter.api.Test
import parser.MyParser
import parser.Parser
import parser.ParserCommons


class ParserTest {

    @Test
    fun test001_testSimpleLiteralVariableDeclarationInt(){
        val code = "let a: int = 5;" // range 14, including spaces...
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 14),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "int", LiteralArgument(Range(13, 13), "5", "int"))
            ))

        println(expected.range.start)
        println(expected.range.end)
        kotlin.test.assertEquals(expected, ast)
    }
    @Test
    fun test002_testSimpleLiteralVariableDeclarationBool(){
        val code = "let a: boolean = true;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 21),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "boolean", LiteralArgument(Range(17, 20), "true", "boolean"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }


    @Test
    fun test003_testSimpleLiteralVariableDeclarationFloat(){
        val code = "let a: float = 3.5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 18),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "float", LiteralArgument(Range(15, 17), "3.5", "float"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test004_testSimpleLiteralVariableDeclarationStringOfOneWord(){
        val code = "let a: string = \"Juan\";"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 22),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "string", LiteralArgument(Range(16, 21), "\"Juan\"", "string"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test005_testConsecutiveLiteralDeclarations(){
        val code = "let a: string = \"Juan\"; let b: int = 5; let c: boolean = true;"
        val tokens = LexerImpl().tokenize(code)

        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 61),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "string", LiteralArgument(Range(16, 21), "\"Juan\"", "string")),
                VariableDeclaration( Range(28, 28), "b", "int", LiteralArgument(Range(37, 37), "5", "int")),
                VariableDeclaration( Range(44, 44), "c", "boolean", LiteralArgument(Range(57, 60), "true", "boolean"))
            ))

        println(ast.body)
        println(expected.body)
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test006_testDeclarationWithVariableArgument(){
        val code = "let a: int = b;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 14), // Adjusted range
            listOf(
                VariableDeclaration(Range(4, 4), "a", "int", VariableArgument(Range(13, 13), "b")))
        )

        kotlin.test.assertEquals(expected, ast)
    }


    // see, it doesn't check if the type of the variable is correct
    // but I don't think that is something for the parser to check
    @Test
    fun test007_testDeclarationsWithLiteralAndVariableArguments(){
        val code = "let a: int = 5; let b: string = a;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 33),
            listOf(
                VariableDeclaration( Range(4, 4), "a", "int", LiteralArgument(Range(13, 13), "5", "int")),
                VariableDeclaration( Range(20, 20), "b", "string", VariableArgument(Range(32, 32), "a"))
            ))

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test008_testSumMethodDeclaration(){
        val code = "let sum: int = 3 + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 20),
            listOf(
                VariableDeclaration(Range(4, 6), "sum", "int", MethodResult(Range(17, 17), Call((Range(15, 19)), "+", listOf(LiteralArgument(Range(15, 15), "3", "int"), LiteralArgument(Range(19, 19), "5", "int")))))));

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test009_testSumMethodDeclarationWithVariable(){
        val code = "let sum: int = a + 5;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 20),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int", MethodResult(Range(17, 17), Call((Range(15, 19)), "+", listOf(VariableArgument(Range(15, 15), "a"), LiteralArgument(Range(19, 19), "5", "int")))))));


        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test010_testSumAndMultiplicationMethodDeclaration(){
        val code = "let sum: int = 3 + 5 * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 24),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(17, 17), Call((Range(15, 23)), "+",
                        listOf( LiteralArgument(Range(15, 15), "3", "int"),
                                MethodResult(Range(21, 21), Call((Range(19, 23)), "*",
                                    listOf( LiteralArgument(Range(19, 19), "5", "int"),
                                            LiteralArgument(Range(23, 23), "2", "int"))))))))));

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test011_testMultiplicationAndSumMethodDeclaration(){
        val code = "let sum: int = 3 * 5 + 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 24),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(21, 21), Call((Range(15, 23)), "+",
                        listOf(MethodResult(Range(17, 17), Call((Range(15, 19)), "*",
                            listOf( LiteralArgument(Range(15, 15), "3", "int"),
                                    LiteralArgument(Range(19, 19), "5", "int")))),
                            LiteralArgument(Range(23, 23), "2", "int")))))));
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test012_testVeryComplexMethodDeclarationWithOutParentheses(){
        val code = "let sum: int = 3 * 5 + 2 - 4 / 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 32),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(21, 21), Call((Range(15, 31)), "+",
                        listOf(MethodResult(Range(17, 17), Call((Range(15, 19)), "*",
                            listOf( LiteralArgument(Range(15, 15), "3", "int"),
                                    LiteralArgument(Range(19, 19), "5", "int")))),
                            MethodResult(Range(25, 25), Call((Range(23, 31)), "-",
                                    listOf(LiteralArgument(Range(23, 23), "2", "int"),
                                        MethodResult(Range(29, 29), Call((Range(27, 31)), "/",
                                            listOf( LiteralArgument(Range(27, 27), "4", "int"),
                                                    LiteralArgument(Range(31, 31), "2", "int")))))))))))));
        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test013_testSearchForClosingCharacter(){
        val code = "((()))))"
        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        val gotChar = commons.searchForClosingCharacter(tokens,"(", 0)
        val expected = 5
        kotlin.test.assertEquals(expected, gotChar)
    }

    @Test
    fun test014_testMethodDeclarationWithParentheses(){
        val code = "let sum: int = (3 + 5) * 2;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 26),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(23, 23), Call(Range(15,25), "*",
                        listOf( MethodResult(Range(18, 18), Call((Range(16, 20)), "+",
                            listOf( LiteralArgument(Range(16, 16), "3", "int"),
                                    LiteralArgument(Range(20, 20), "5", "int")))),
                                LiteralArgument(Range(25, 25), "2", "int")))))));

        kotlin.test.assertEquals(expected, ast)
    }
    @Test
    fun test015_testMethodDeclarationMultOfTwoParentheses(){
        val code = "let sum: int = (3 + 5) * (2 + 4);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 32),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(23, 23), Call(Range(15,31), "*",
                        listOf( MethodResult(Range(18, 18), Call((Range(16, 20)), "+",
                                    listOf( LiteralArgument(Range(16, 16), "3", "int"),
                                            LiteralArgument(Range(20, 20), "5", "int")))),
                                MethodResult(Range(28, 28), Call(Range(26,30), "+",
                                    listOf( LiteralArgument(Range(26, 26), "2", "int"),
                                            LiteralArgument(Range(30, 30), "4", "int"))))))))));

        kotlin.test.assertEquals(expected, ast)
    }


    // solucionar el tema de que en realidad si usa parentesis non meterlos como method result. El parentesis no es un operator.
    @Test
    fun test016_testParenthesesInsideParentheses(){
        val code = "let sum: int = ((3 + 5) * (2 + 4));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 34),
            listOf(
                VariableDeclaration( Range(4, 6), "sum", "int",
                    MethodResult(Range(24, 24), Call(Range(16,32), "*",
                        listOf( MethodResult(Range(19, 19), Call((Range(17, 21)), "+",
                                    listOf( LiteralArgument(Range(17, 17), "3", "int"),
                                            LiteralArgument(Range(21, 21), "5", "int")))),
                                MethodResult(Range(29, 29), Call(Range(27,31), "+",
                                    listOf( LiteralArgument(Range(27, 27), "2", "int"),
                                            LiteralArgument(Range(31, 31), "4", "int"))))))))));

        kotlin.test.assertEquals(expected, ast)
    }




    // falta resolver el tema de las comas. No son operadores, son SEPARADORES.
    // Quiza, en el getFinalArgumentsOfMethodResult fijarse en vez del tamaño, si hay operadores.
    @Test
    fun test017_testMethodDeclarationOfAMethodCall(){
        val code = "let test: int = sum(3, 5);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 25),
            listOf(
                VariableDeclaration( Range(4, 7), "test", "int",
                    MethodResult(Range(16, 18), Call(Range(20, 23), "sum",
                        listOf( LiteralArgument(Range(20, 20), "3", "int"),
                                LiteralArgument(Range(23, 23), "5", "int")))))));

        kotlin.test.assertEquals(expected, ast)
    }

    @Test
    fun test018_testMethodDeclarationWithOperationInside(){
        val code = "let test: int = sum(3, 5 + 2);"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 29),
            listOf(
                VariableDeclaration( Range(4, 7), "test", "int",
                    MethodResult(Range(16, 18), Call(Range(20, 27), "sum",
                        listOf( LiteralArgument(Range(20, 20), "3", "int"),
                                MethodResult(Range(25, 25), Call(Range(23, 27), "+",
                                    listOf( LiteralArgument(Range(23, 23), "5", "int"),
                                            LiteralArgument(Range(27, 27), "2", "int"))))))))));

        kotlin.test.assertEquals(expected, ast)
    }


    @Test
    fun test019_testMethodInsideMethod(){
        val code = "let test: int = sum(3, sum(5, 2));"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 33),
            listOf(
                VariableDeclaration( Range(4, 7), "test", "int",
                    MethodResult(Range(16, 18), Call(Range(20, 31), "sum",
                        listOf( LiteralArgument(Range(20, 20), "3", "int"),
                            MethodResult(Range(23, 25), Call(Range(27, 30), "sum",
                                listOf( LiteralArgument(Range(27, 27), "5", "int"),
                                    LiteralArgument(Range(30, 30), "2", "int"))))))))));

        kotlin.test.assertEquals(expected, ast)
    }


    // lo que pasa en sum(3, sum(5, 2)) es que ya de una te toma tres metodos: 3, sum(5 y 2)
    @Test
    fun test020_testSearchForClosingBracketsMethod(){
        val code = "let test: int = sum(3, sum(5, 2));"

        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        println(tokens[6].token.text)
        println(tokens[15].token.text)
        val gotChar = commons.searchForClosingCharacter(tokens,"(", 6)
        val expected = 15
        kotlin.test.assertEquals(expected, gotChar)
    }

    @Test
    fun test021_testSearchForClosingBracketsMethodPt2(){
        val code = "let test: int = sum(3, sum(5, 2));"

        val tokens = LexerImpl().tokenize(code)
        val commons = ParserCommons()
        println(tokens[14].token.text)
        val gotChar = commons.searchForClosingCharacter(tokens,"(", 10)
        val expected = 14
        kotlin.test.assertEquals(expected, gotChar)
    }

    @Test
    fun test022_testComplexEcuationWithParenthesesButParenthesesAreNotIncludedInTheAST(){
        val code = "let test: int = 3 + (5 * 2) + 4;"
        val tokens = LexerImpl().tokenize(code)
        val parser: Parser = MyParser()
        val ast = parser.parseTokens(tokens)
        val expected = Scope(
            "program",
            Range(0, 31),
            listOf(
                VariableDeclaration( Range(4, 7), "test", "int",
                    MethodResult(Range(18, 18), Call(Range(16, 30), "+",
                        listOf( LiteralArgument(Range(16, 16), "3", "int"),
                            MethodResult(Range(28, 28), Call(Range(20, 30), "+",
                                listOf( MethodResult(Range(23, 23), Call(Range(21, 25), "*",
                                    listOf( LiteralArgument(Range(21, 21), "5", "int"),
                                        LiteralArgument(Range(25, 25), "2", "int")))),
                                    LiteralArgument(Range(30, 30), "4", "int"))))))))));

        kotlin.test.assertEquals(expected, ast)
    }

}