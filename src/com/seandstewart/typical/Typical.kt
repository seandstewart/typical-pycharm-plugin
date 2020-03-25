package com.seandstewart.typical

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveResult
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.QualifiedName
import com.jetbrains.python.codeInsight.typing.PyTypingTypeProvider
import com.jetbrains.python.psi.*
import com.jetbrains.python.psi.resolve.PyResolveContext
import com.jetbrains.python.psi.resolve.PyResolveUtil
import com.jetbrains.python.psi.types.PyClassType
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.PyUnionType
import com.jetbrains.python.psi.types.TypeEvalContext

const val TYPIC_KLASS_Q_NAME = "typic.klass"
const val TYPIC_SETTINGS_Q_NAME = "typic.settings"
const val TYPIC_CONTRAINED_Q_NAME = "typic.constrained"
const val FIELD_Q_NAME = "typic.klass.Field"


fun getPyClassByPyCallExpression(pyCallExpression: PyCallExpression, context: TypeEvalContext): PyClass? {
    val callee = pyCallExpression.callee ?: return null
    val pyType = when (val type = context.getType(callee)) {
        is PyClass -> return type
        is PyClassType -> type
        else -> (callee.reference?.resolve() as? PyTypedElement)?.let { context.getType(it) } ?: return null
    }
    return getPyClassTypeByPyTypes(pyType).firstOrNull { isTypicKlass(it.pyClass) }?.pyClass
}

fun getPyClassByPyKeywordArgument(pyKeywordArgument: PyKeywordArgument, context: TypeEvalContext): PyClass? {
    val pyCallExpression = PsiTreeUtil.getParentOfType(pyKeywordArgument, PyCallExpression::class.java) ?: return null
    return getPyClassByPyCallExpression(pyCallExpression, context)
}

fun isTypicKlass(pyClass: PyClass, context: TypeEvalContext? = null): Boolean {
    return hasDecorator(pyClass, TYPIC_KLASS_Q_NAME) || isTypicSettings(pyClass)
}

fun isTypicSettings(pyClass: PyClass, context: TypeEvalContext? = null): Boolean {
    return hasDecorator(pyClass, TYPIC_SETTINGS_Q_NAME)
}

internal fun hasDecorator(pyDecoratable: PyDecoratable, refName: String): Boolean {
    pyDecoratable.decoratorList?.decorators?.mapNotNull { it.callee as? PyReferenceExpression }?.forEach {
        PyResolveUtil.resolveImportedElementQNameLocally(it).forEach { decoratorQualifiedName ->
            if (decoratorQualifiedName == QualifiedName.fromDottedString(refName)) return true
        }
    }
    return false
}

internal fun isTypicField(pyFunction: PyFunction): Boolean {
    return pyFunction.qualifiedName == FIELD_Q_NAME
}

internal fun getClassVariables(pyClass: PyClass, context: TypeEvalContext): Sequence<PyTargetExpression> {
    return pyClass.classAttributes
            .asReversed()
            .asSequence()
            .filterNot { PyTypingTypeProvider.isClassVar(it, context) }
}

fun getResolveElements(referenceExpression: PyReferenceExpression, context: TypeEvalContext): Array<ResolveResult> {
    val resolveContext = PyResolveContext.defaultContext().withTypeEvalContext(context)
    return referenceExpression.getReference(resolveContext).multiResolve(false)

}

fun getPyClassTypeByPyTypes(pyType: PyType): List<PyClassType> {
    return when (pyType) {
        is PyUnionType ->
            pyType.members
                    .mapNotNull { it }
                    .flatMap {
                        getPyClassTypeByPyTypes(it)
                    }
        is PyClassType -> listOf(pyType)
        else -> listOf()
    }
}

fun isTypicFieldByPsiElement(psiElement: PsiElement): Boolean {
    when (psiElement) {
        is PyFunction -> return isTypicField(psiElement)
        else -> PsiTreeUtil.getContextOfType(psiElement, PyFunction::class.java)
                ?.let { return isTypicField(it) }
    }
    return false
}


fun getPyClassByAttribute(pyPsiElement: PsiElement?): PyClass? {
    return pyPsiElement?.parent?.parent as? PyClass
}
