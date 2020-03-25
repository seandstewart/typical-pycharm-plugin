package com.seandstewart.typical

import com.jetbrains.python.inspections.PyInspection
import kotlin.reflect.KClass


abstract class TypicalInspectionBase : TypicalTestCase() {

    @Suppress("UNCHECKED_CAST")
    protected open val inspectionClass: KClass<PyInspection> = TypicalInspection::class as KClass<PyInspection>

    private fun configureInspection() {
        myFixture!!.enableInspections(inspectionClass.java)
        myFixture!!.checkHighlighting(true, false, true)

    }

    protected fun doTest() {
        configureByFile()
        configureInspection()
    }
}
