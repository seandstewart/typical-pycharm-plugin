package com.seandstewart.typical

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent


class TypicalConfigurable internal constructor(project: Project) : Configurable {
    private val typicalConfigService: TypicalConfigService = TypicalConfigService.getInstance(project)
    private val configPanel: TypicalConfigPanel = TypicalConfigPanel(project)
    override fun getDisplayName(): String {
        return "Typical"
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent? {
        reset()
        return configPanel.configPanel
    }

    override fun reset() {}

    override fun isModified(): Boolean {
        if (configPanel.initTyped == null) return false
        return  (typicalConfigService.initTyped != configPanel.initTyped)
    }

    override fun apply() {
        typicalConfigService.initTyped = configPanel.initTyped
    }

    override fun disposeUIResources() {
    }
}