import ast.AssignmentStatement
import ast.BooleanCondition
import ast.Call
import ast.ConstantDeclaration
import ast.DeclarationStatement
import ast.IfAndElseStatement
import ast.IfStatement
import ast.LiteralArgument
import ast.MethodResult
import ast.Range
import ast.Scope
import ast.VariableArgument
import ast.VariableDeclaration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ASTTestToString {
    @Test
    fun `test toString of Range`() {
        val range = Range(1, 2)
        assertEquals("Range(start=1, end=2)", range.toString())
    }

    @Test
    fun `test toString of Scope`() {
        val scope = Scope("program", Range(0, 2), emptyList())
        assertEquals(
            "Scope(type='program', range=Range(start=0, end=2), body=[])",
            scope.toString(),
        )
    }

    @Test
    fun `test toString of BooleanCondition`() {
        val booleanCond =
            BooleanCondition(Range(3, 3), VariableArgument(Range(3, 3), "a"))
        assertEquals(
            "BooleanCondition(range=Range(start=3, end=3)," +
                " argument=VariableArgument(range=Range(start=3, end=3), name='a'))",
            booleanCond.toString(),
        )
    }

    @Test
    fun `test toString of Call`() {
        val call =
            Call(
                Range(4, 6),
                "someMethod",
                listOf(
                    VariableArgument(Range(4, 4), "arg1"),
                    LiteralArgument(Range(5, 5), "5", "Int"),
                ),
            )
        assertEquals(
            "Call(range=Range(start=4, end=6), name='someMethod'," +
                " arguments=[VariableArgument(range=Range(start=4, end=4), name='arg1')," +
                " LiteralArgument(range=Range(start=5, end=5), value='5', type='Int')])",
            call.toString(),
        )
    }

    @Test
    fun `test toString of VariableDeclaration`() {
        val varDecl =
            VariableDeclaration(
                Range(7, 9),
                "variable1",
                "Int",
                LiteralArgument(Range(8, 8), "10", "Int"),
            )
        assertEquals(
            "VariableDeclaration(range=Range(start=7, end=9)," +
                " variableName='variable1', variableType='Int'," +
                " value=LiteralArgument(range=Range(start=8, end=8)," +
                " value='10', type='Int'))",
            varDecl.toString(),
        )
    }

    @Test
    fun `test toString of ConstantDeclaration`() {
        val constDecl =
            ConstantDeclaration(
                Range(10, 12),
                "constant1",
                "String",
                LiteralArgument(Range(11, 11), "\"Hello\"", "String"),
            )
        assertEquals(
            "ConstantDeclaration(range=Range(start=10, end=12)," +
                " variableName='constant1', variableType='String'," +
                " value=LiteralArgument(range=Range(start=11, end=11)," +
                " value='\"Hello\"', type='String'))",
            constDecl.toString(),
        )
    }

    @Test
    fun `test toString of LiteralArgument`() {
        val literalArg = LiteralArgument(Range(13, 13), "true", "Boolean")
        assertEquals(
            "LiteralArgument(range=Range(start=13, end=13), value='true', type='Boolean')",
            literalArg.toString(),
        )
    }

    @Test
    fun `test toString of VariableArgument`() {
        val varArg = VariableArgument(Range(14, 14), "variable2")
        assertEquals(
            "VariableArgument(range=Range(start=14, end=14), name='variable2')",
            varArg.toString(),
        )
    }

    @Test
    fun `test toString of MethodResult`() {
        val methodResult =
            MethodResult(
                Range(15, 17),
                Call(Range(16, 17), "getResult", emptyList()),
            )
        assertEquals(
            "MethodResult(range=Range(start=15, end=17)," +
                " methodCall=Call(range=Range(start=16, end=17)," +
                " name='getResult', arguments=[]))",
            methodResult.toString(),
        )
    }

    @Test
    fun `test toString of DeclarationStatement`() {
        val declStmt =
            DeclarationStatement(Range(18, 20), "variable3", "Double")
        assertEquals(
            "DeclarationStatement(range=Range(start=18, end=20)," +
                " variableName='variable3', variableType='Double')",
            declStmt.toString(),
        )
    }

    @Test
    fun `test toString of AssignmentStatement`() {
        val assignStmt =
            AssignmentStatement(
                Range(21, 23),
                "variable4",
                LiteralArgument(Range(22, 22), "7", "Int"),
            )
        assertEquals(
            "AssignmentStatement(range=Range(start=21, end=23)," +
                " variableName='variable4'," +
                " value=LiteralArgument(range=Range(start=22, end=22)," +
                " value='7', type='Int'))",
            assignStmt.toString(),
        )
    }

    @Test
    fun `test toString of IfStatement`() {
        val ifStmt =
            IfStatement(
                Range(24, 26),
                listOf(
                    BooleanCondition(
                        Range(25, 25),
                        LiteralArgument(Range(25, 25), "true", "Boolean"),
                    ),
                ),
                Scope("ifScope", Range(26, 26), emptyList()),
            )
        assertEquals(
            "IfStatement(range=Range(start=24, end=26)," +
                " conditions=[BooleanCondition(range=Range(start=25, end=25)," +
                " argument=LiteralArgument(range=Range(start=25, end=25)," +
                " value='true', type='Boolean'))]," +
                " scope=Scope(type='ifScope', range=Range(start=26, end=26)," +
                " body=[]))",
            ifStmt.toString(),
        )
    }

    @Test
    fun `test toString of IfAndElseStatement`() {
        val ifElseStmt =
            IfAndElseStatement(
                Range(27, 29),
                listOf(
                    BooleanCondition(
                        Range(28, 28),
                        LiteralArgument(Range(28, 28), "false", "Boolean"),
                    ),
                ),
                Scope("ifScope", Range(29, 29), emptyList()),
                Scope("elseScope", Range(29, 29), emptyList()),
            )
        assertEquals(
            "IfAndElseStatement(range=Range(start=27, end=29), " +
                "conditions=[BooleanCondition(range=Range(start=28, end=28), " +
                "argument=LiteralArgument(range=Range(start=28, end=28), value='false', type='Boolean'))], " +
                "ifScope=Scope(type='ifScope', range=Range(start=29, end=29), body=[]), " +
                "elseScope=Scope(type='elseScope', range=Range(start=29, end=29), body=[]))",
            ifElseStmt.toString(),
        )
    }

    // Add more tests for other classes as needed
}
