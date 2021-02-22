package nl.hannahsten.texifyidea.inspections.latex

import nl.hannahsten.texifyidea.file.LatexFileType
import nl.hannahsten.texifyidea.inspections.TexifyInspectionTestBase
import nl.hannahsten.texifyidea.testutils.writeCommand

class LatexEncloseWithLeftRightInspectionTest : TexifyInspectionTestBase(LatexEncloseWithLeftRightInspection()) {

    fun testWarning() {
        myFixture.configureByText(
            LatexFileType,
            """
            ${'$'} <warning descr="Parentheses pair could be replaced by \left(..\right)">(</warning>\frac 1 2<warning descr="Parentheses pair could be replaced by \left(..\right)">)</warning>${'$'}
            """.trimIndent()
        )
        myFixture.checkHighlighting()
    }

    fun testQuickfix() {
        myFixture.configureByText(
            LatexFileType,
            """
            ${'$'} (\frac 1 2)${'$'}
            """.trimIndent()
        )

        val quickFixes = myFixture.getAllQuickFixes()
        assertEquals(2, quickFixes.size)
        writeCommand(myFixture.project) {
            quickFixes.first().invoke(myFixture.project, myFixture.editor, myFixture.file)
        }

        myFixture.checkResult(
            """
            ${'$'} \left(\frac 1 2\right)${'$'}
            """.trimIndent()
        )
    }
}