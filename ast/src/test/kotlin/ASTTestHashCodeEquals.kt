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

class ASTTestHashCodeEquals {
    @Test
    fun `test hashCode of Range`() {
        val range1 = Range(1, 2)
        val range2 = Range(1, 2)
        assertEquals(range1.hashCode(), range2.hashCode())
    }

    @Test
    fun `test hashCode of Scope`() {
        val scope1 = Scope("program", Range(0, 2), emptyList())
        val scope2 = Scope("program", Range(0, 2), emptyList())
        assertEquals(scope1.hashCode(), scope2.hashCode())
    }

    @Test
    fun `test hashCode of Call`() {
        val call1 = Call(Range(4, 6), "someMethod", emptyList())
        val call2 = Call(Range(4, 6), "someMethod", emptyList())
        assertEquals(call1.hashCode(), call2.hashCode())
    }

    @Test
    fun `test hashCode of VariableDeclaration`() {
        val varDecl1 =
            VariableDeclaration(
                Range(7, 9),
                "variable1",
                "Int",
                LiteralArgument(Range(8, 8), "10", "Int"),
            )
        val varDecl2 =
            VariableDeclaration(
                Range(7, 9),
                "variable1",
                "Int",
                LiteralArgument(Range(8, 8), "10", "Int"),
            )
        assertEquals(varDecl1.hashCode(), varDecl2.hashCode())
    }

    @Test
    fun `test hashCode of ConstantDeclaration`() {
        val constDecl1 =
            ConstantDeclaration(
                Range(10, 12),
                "constant1",
                "String",
                LiteralArgument(Range(11, 11), "\"Hello\"", "String"),
            )
        val constDecl2 =
            ConstantDeclaration(
                Range(10, 12),
                "constant1",
                "String",
                LiteralArgument(Range(11, 11), "\"Hello\"", "String"),
            )
        assertEquals(constDecl1.hashCode(), constDecl2.hashCode())
    }

    @Test
    fun `test hashCode of LiteralArgument`() {
        val literalArg1 = LiteralArgument(Range(13, 13), "true", "Boolean")
        val literalArg2 = LiteralArgument(Range(13, 13), "true", "Boolean")
        assertEquals(literalArg1.hashCode(), literalArg2.hashCode())
    }

    @Test
    fun `test hashCode of VariableArgument`() {
        val varArg1 = VariableArgument(Range(14, 14), "variable2")
        val varArg2 = VariableArgument(Range(14, 14), "variable2")
        assertEquals(varArg1.hashCode(), varArg2.hashCode())
    }

    @Test
    fun `test hashCode of MethodResult`() {
        val methodResult1 =
            MethodResult(
                Range(15, 17),
                Call(Range(16, 17), "getResult", emptyList()),
            )
        val methodResult2 =
            MethodResult(
                Range(15, 17),
                Call(Range(16, 17), "getResult", emptyList()),
            )
        assertEquals(methodResult1.hashCode(), methodResult2.hashCode())
    }

    @Test
    fun `test hashCode of DeclarationStatement`() {
        val declStmt1 =
            DeclarationStatement(Range(18, 20), "variable3", "Double")
        val declStmt2 =
            DeclarationStatement(Range(18, 20), "variable3", "Double")
        assertEquals(declStmt1.hashCode(), declStmt2.hashCode())
    }

    @Test
    fun `test hashCode of AssignmentStatement`() {
        val assignStmt1 =
            AssignmentStatement(
                Range(21, 23),
                "variable4",
                LiteralArgument(Range(22, 22), "7", "Int"),
            )
        val assignStmt2 =
            AssignmentStatement(
                Range(21, 23),
                "variable4",
                LiteralArgument(Range(22, 22), "7", "Int"),
            )
        assertEquals(assignStmt1.hashCode(), assignStmt2.hashCode())
    }

    @Test
    fun `test hashCode of BooleanCondition`() {
        val booleanCond1 =
            BooleanCondition(
                Range(25, 25),
                LiteralArgument(Range(25, 25), "true", "Boolean"),
            )
        val booleanCond2 =
            BooleanCondition(
                Range(25, 25),
                LiteralArgument(Range(25, 25), "true", "Boolean"),
            )
        assertEquals(booleanCond1.hashCode(), booleanCond2.hashCode())
    }

    @Test
    fun `test hashCode of IfStatement`() {
        val ifStmt1 =
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
        val ifStmt2 =
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
        assertEquals(ifStmt1.hashCode(), ifStmt2.hashCode())
    }

    @Test
    fun `test hashCode of IfAndElseStatement`() {
        val ifElseStmt1 =
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
        val ifElseStmt2 =
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
        assertEquals(ifElseStmt1.hashCode(), ifElseStmt2.hashCode())
    }
}
