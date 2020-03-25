package com.seandstewart.typical;

import com.intellij.openapi.project.Project;

import javax.swing.*;

public class TypicalConfigPanel {

    TypicalConfigPanel(Project project) {
        TypicalConfigService typicalConfigService = TypicalConfigService.Companion.getInstance(project);

        this.initTypedCheckBox.setSelected(typicalConfigService.getInitTyped());

    }

    private JPanel configPanel;
    private JCheckBox initTypedCheckBox;
    private JTextPane ifEnabledIncludeTheTextPane;
    private JTextPane ifEnabledRaiseATextPane;

    public Boolean getInitTyped() {
        return initTypedCheckBox.isSelected();
    }

    public JPanel getConfigPanel() {
        return configPanel;
    }

}
