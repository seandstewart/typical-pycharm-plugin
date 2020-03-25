package com.seandstewart.typical


open class TypicalRefactorTest : TypicalTestCase() {


    private fun checkResultByFile() {
        myFixture!!.checkResultByFile("${testDataMethodPath}_after.py")

    }

    private fun doRefactorTest(newName: String = "cde", isApplicable: Boolean = true, skipCheck: Boolean =false) {
        configureByFile()
        val pydanticFieldRenameFactory = TypicalFieldRenameFactory()
        if (!skipCheck) {
            assertEquals(pydanticFieldRenameFactory.isApplicable(myFixture!!.elementAtCaret), isApplicable)

            if (!isApplicable) return
        }

        pydanticFieldRenameFactory.createRenamer(myFixture!!.elementAtCaret, newName, ArrayList()).renames.forEach { (t, u) ->
            if (t.name != newName) {
                myFixture!!.renameElement(t, u)
            }
        }
        checkResultByFile()
    }

    fun testRenameField() {
        doRefactorTest()
    }

    fun testRenameKeywordArgument() {
        doRefactorTest()
    }

    fun testRenameFieldDataclass() {
        doRefactorTest()
    }

    fun testRenameKeywordArgumentDataclass() {
        doRefactorTest()
    }

    fun testRenameGrandChildFieldWithPythonClass() {
        doRefactorTest()
    }

    fun testRenamePythonClass() {
        doRefactorTest(isApplicable = false)
    }

    fun testRenamePythonClassKeywordArgument() {
        doRefactorTest(isApplicable = false)
    }

    fun testRenamePythonFunction() {
        doRefactorTest(isApplicable = false)
    }

    fun testRenamePythonFunctionKeywordArgument() {
        doRefactorTest(isApplicable = false)
    }

    fun testRenameUnResolve() {
        doRefactorTest(isApplicable = false)
        doRefactorTest(skipCheck = true)
    }

    fun testRenameUnResolveTargetExpression() {
        doRefactorTest(isApplicable = false)
        doRefactorTest(skipCheck = true)

    }

    fun testGetOptionName() {
        assertEquals(TypicalFieldRenameFactory().optionName, "Rename fields in hierarchy")
    }

    fun testIsEnabled() {
        assertTrue(TypicalFieldRenameFactory().isEnabled)
    }

    fun testSetEnabled() {
        val pydanticFieldRenameFactory = TypicalFieldRenameFactory()
        pydanticFieldRenameFactory.isEnabled = false
        assertFalse(pydanticFieldRenameFactory.isEnabled)
    }

    fun testRenamer() {
        configureByFile()
        val pydanticFieldRenameFactory = TypicalFieldRenameFactory()
        val renamer = pydanticFieldRenameFactory.createRenamer(myFixture!!.elementAtCaret, "newName", ArrayList())
        assertEquals(renamer.dialogTitle, "Rename Fields")
        assertEquals(renamer.dialogDescription, "Rename field in hierarchy to:")
        assertEquals(renamer.entityName(), "Field")
        assertEquals(renamer.isSelectedByDefault, true)
    }
}
