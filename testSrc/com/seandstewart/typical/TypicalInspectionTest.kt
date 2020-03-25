package com.seandstewart.typical

import com.jetbrains.python.inspections.PyInspection
import kotlin.reflect.KClass


open class TypicalInspectionTest : TypicalInspectionBase() {

    @Suppress("UNCHECKED_CAST")
    override val inspectionClass: KClass<PyInspection> = TypicalInspection::class as KClass<PyInspection>

    fun testPythonClass() {
        doTest()
    }

}
