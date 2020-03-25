package com.seandstewart.typical

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.codeInsight.completion.getTypeEvalContext
import com.jetbrains.python.documentation.PythonDocumentationProvider.getTypeHint
import com.jetbrains.python.psi.*
import com.jetbrains.python.psi.types.PyClassType
import com.jetbrains.python.psi.types.TypeEvalContext
import javax.swing.Icon


class TypicalCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC,
                psiElement(PyTokenTypes.IDENTIFIER).withParents(
                        PyReferenceExpression::class.java,
                        PyArgumentList::class.java,
                        PyCallExpression::class.java),
                KeywordArgumentCompletionProvider)
        extend(CompletionType.BASIC,
                psiElement(PyTokenTypes.IDENTIFIER)
                        .afterLeaf(psiElement(PyTokenTypes.DOT))
                        .withParent(psiElement(PyReferenceExpression::class.java)),
                FieldCompletionProvider)
    }

    private abstract class TypicCompletionProvider : CompletionProvider<CompletionParameters>() {

        abstract val icon: Icon

        abstract fun getLookupNameFromFieldName(field: PyTargetExpression, context: TypeEvalContext): String

        val typeProvider: TypicalTypeProvider = TypicalTypeProvider()

        val excludeFields: HashSet<String> = hashSetOf("Config")

        private fun getTypeText(pyClass: PyClass, typeEvalContext: TypeEvalContext,
                                pyTargetExpression: PyTargetExpression,
                                ellipsis: PyNoneLiteralExpression): String {

            val parameter = typeProvider.fieldToParameter(pyTargetExpression, ellipsis, typeEvalContext, pyClass)
            val defaultValue = parameter?.defaultValue?.let {
                if (parameter.defaultValue is PyNoneLiteralExpression) {
                    "=None"
                } else if (isTypicSettings(pyClass, typeEvalContext)){
                    "=..."
                }
                else {
                    "=${parameter.defaultValueText}"
                }
            } ?: ""
            val typeHint = getTypeHint(parameter?.getType(typeEvalContext), typeEvalContext)
            return "${typeHint}$defaultValue ${pyClass.name}"
        }


        private fun addFieldElement(pyClass: PyClass, results: LinkedHashMap<String, LookupElement>,
                                    typeEvalContext: TypeEvalContext,
                                    ellipsis: PyNoneLiteralExpression,
                                    excludes: HashSet<String>?) {
            getClassVariables(pyClass, typeEvalContext)
                    .filter { it.name != null }
                    .forEach {
                        val elementName = getLookupNameFromFieldName(it, typeEvalContext)
                        if (excludes == null || !excludes.contains(elementName)) {
                            val element = PrioritizedLookupElement.withGrouping(
                                    LookupElementBuilder
                                            .createWithSmartPointer(elementName, it)
                                            .withTypeText(getTypeText(pyClass, typeEvalContext, it, ellipsis))
                                            .withIcon(icon), 1)
                            results[elementName] = PrioritizedLookupElement.withPriority(element, 100.0)
                        }
                    }
        }

        protected fun addAllFieldElement(parameters: CompletionParameters, result: CompletionResultSet,
                                         pyClass: PyClass, typeEvalContext: TypeEvalContext,
                                         ellipsis: PyNoneLiteralExpression,
                                         excludes: HashSet<String>? = null) {

            val newElements: LinkedHashMap<String, LookupElement> = LinkedHashMap()

            pyClass.getAncestorClasses(typeEvalContext)
                    .filter { isTypicKlass(it) }
                    .forEach { addFieldElement(it, newElements, typeEvalContext,
                            ellipsis, excludes) }

            addFieldElement(pyClass, newElements, typeEvalContext, ellipsis, excludes)

            result.runRemainingContributors(parameters)
            { completionResult ->
                completionResult.lookupElement.lookupString
                        .takeIf { name -> !newElements.containsKey(name) && (excludes == null || !excludes.contains(name)) }
                        ?.let { result.passResult(completionResult) }
            }
            result.addAllElements(newElements.values)
        }

        protected fun removeAllFieldElement(parameters: CompletionParameters, result: CompletionResultSet,
                                            pyClass: PyClass, typeEvalContext: TypeEvalContext,
                                            excludes: HashSet<String>) {

            if (!isTypicKlass(pyClass)) return

            val fieldElements: HashSet<String> = HashSet()

            pyClass.getAncestorClasses(typeEvalContext)
                    .filter { isTypicKlass(it) }
                    .forEach { fieldElements.addAll(it.classAttributes
                            .mapNotNull { attribute -> attribute?.name }) }



            fieldElements.addAll(pyClass.classAttributes
                    .mapNotNull { attribute -> attribute?.name })

            result.runRemainingContributors(parameters)
            { completionResult ->
                if (completionResult.lookupElement.psiElement?.getIcon(0) == AllIcons.Nodes.Field) {
                    completionResult.lookupElement.lookupString
                            .takeIf { name -> !fieldElements.contains(name) && (!excludes.contains(name)) }
                            ?.let { result.passResult(completionResult) }
                } else {
                    result.passResult(completionResult)
                }
            }
        }
    }

    private object KeywordArgumentCompletionProvider : TypicCompletionProvider() {
        override fun getLookupNameFromFieldName(field: PyTargetExpression, context: TypeEvalContext): String {
            return "${field.name}="
        }

        override val icon: Icon = AllIcons.Nodes.Parameter

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val pyArgumentList = parameters.position.parent?.parent as? PyArgumentList ?: return
            val typeEvalContext = parameters.getTypeEvalContext()
            val pyClassType = (pyArgumentList.parent as? PyCallExpression)?.let{typeEvalContext.getType(it)} as? PyClassType
                    ?: return

            if (!isTypicKlass(pyClassType.pyClass, typeEvalContext)) return

            val definedSet = pyArgumentList.children
                    .mapNotNull { (it as? PyKeywordArgument)?.name }
                    .map { "${it}=" }
                    .toHashSet()
            val ellipsis = PyElementGenerator.getInstance(pyClassType.pyClass.project).createEllipsis()
            addAllFieldElement(parameters, result, pyClassType.pyClass, typeEvalContext, ellipsis)
        }
    }

    private object FieldCompletionProvider : TypicCompletionProvider() {
        override fun getLookupNameFromFieldName(field: PyTargetExpression, context: TypeEvalContext): String {
            return field.name!!
        }

        override val icon: Icon = AllIcons.Nodes.Field

        override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
            val typeEvalContext = parameters.getTypeEvalContext()
            val pyType = (parameters.position.parent?.firstChild as? PyTypedElement)?.let { typeEvalContext.getType(it) }
                    ?: return

            val pyClassType = getPyClassTypeByPyTypes(pyType).firstOrNull { isTypicKlass(it.pyClass) }
                    ?: return
            if (pyClassType.isDefinition) { // class
                removeAllFieldElement(parameters, result, pyClassType.pyClass, typeEvalContext, excludeFields)
                return
            }
            val ellipsis = PyElementGenerator.getInstance(pyClassType.pyClass.project).createEllipsis()
            addAllFieldElement(parameters, result, pyClassType.pyClass, typeEvalContext, ellipsis)
        }
    }

}
