import ast.BooleanCondition
import ast.Call
import ast.IfAndElseStatement
import ast.IfStatement
import ast.LiteralArgument
import ast.Range
import ast.Scope
import ast.VariableDeclaration
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class ASTTestHashCodeNotEquals {
    @Test
    fun `test hashCode inequality of Range`() {
        val range1 = Range(1, 2)
        val range2 = Range(2, 3)
        assertNotEquals(range1.hashCode(), range2.hashCode())
    }

    @Test
    fun `test hashCode inequality of Scope`() {
        val scope1 = Scope("program", Range(0, 2), emptyList())
        val scope2 = Scope("function", Range(0, 2), emptyList())
        assertNotEquals(scope1.hashCode(), scope2.hashCode())
    }

    @Test
    fun `test hashCode inequality of Call`() {
        val call1 = Call(Range(4, 6), "someMethod", emptyList())
        val call2 = Call(Range(4, 6), "anotherMethod", emptyList())
        assertNotEquals(call1.hashCode(), call2.hashCode())
    }

    @Test
    fun `test hashCode inequality of VariableDeclaration`() {
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
                "variable2",
                "Double",
                LiteralArgument(Range(8, 8), "10", "Int"),
            )
        assertNotEquals(varDecl1.hashCode(), varDecl2.hashCode())
    }

    @Test
    fun `test hashCode inequality of LiteralArgument`() {
        val literalArg1 = LiteralArgument(Range(13, 13), "true", "Boolean")
        val literalArg2 = LiteralArgument(Range(13, 13), "false", "Boolean")
        assertNotEquals(literalArg1.hashCode(), literalArg2.hashCode())
    }

    @Test
    fun `test hashCode inequality of IfStatement`() {
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
                        LiteralArgument(Range(25, 25), "false", "Boolean"),
                    ),
                ),
                Scope("ifScope", Range(26, 26), emptyList()),
            )
        assertNotEquals(ifStmt1.hashCode(), ifStmt2.hashCode())
    }

    @Test
    fun `test hashCode inequality of IfAndElseStatement`() {
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
                        LiteralArgument(
                            Range(28, 28),
                            "true",
                            "Boolean",
                        ),
                    ),
                ),
                Scope(
                    "ifScope",
                    Range(29, 29),
                    emptyList(),
                ),
                Scope("elseScope", Range(29, 29), emptyList()),
            )
        assertNotEquals(ifElseStmt1.hashCode(), ifElseStmt2.hashCode())
    }
}
