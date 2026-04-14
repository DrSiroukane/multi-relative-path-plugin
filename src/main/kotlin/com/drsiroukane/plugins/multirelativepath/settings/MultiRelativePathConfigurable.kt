package com.drsiroukane.plugins.multirelativepath.settings

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import java.awt.BorderLayout
import javax.swing.*

class MultiRelativePathConfigurable(
    private val project: Project
) : Configurable {

    private val settings = MultiRelativePathSettings.getInstance(project)

    // UI model
    private val model = DefaultListModel<String>()
    private val list = JBList(model)

    // working copy (IMPORTANT: avoids direct mutation bugs)
    private var workingList: MutableList<String> = mutableListOf()

    override fun getDisplayName(): String = "Multi Relative Path"

    override fun createComponent(): JComponent {

        reset()

        val decorator = ToolbarDecorator.createDecorator(list)

        // =========================
        // ADD (+)
        // =========================
        decorator.setAddAction {
            val dialog = IncludeFoldersDialog(project)

            if (dialog.showAndGet()) {
                val newPaths = dialog.getSelectedPaths()

                var added = false
                for (path in newPaths) {
                    if (!workingList.contains(path)) {
                        workingList.add(path)
                        added = true
                    }
                }

                if (added) {
                    refresh()
                }
            }
        }

        // =========================
        // REMOVE (-)
        // =========================
        decorator.setRemoveAction {

            val selected = list.selectedValue ?: return@setRemoveAction
            workingList.remove(selected)
            refresh()
        }

        return JPanel(BorderLayout()).apply {
            add(decorator.createPanel(), BorderLayout.CENTER)
        }
    }

    // =========================
    // APPLY / RESET
    // =========================
    override fun isModified(): Boolean {
        return workingList != settings.state.parentFolders
    }

    override fun apply() {

        val result = Messages.showYesNoDialog(
            project,
            "Apply changes to Relative Parents?",
            "Multi Relative Path",
            "Yes",
            "No",
            null
        )

        if (result == Messages.YES) {

            settings.state.parentFolders = workingList.toMutableList()

            // 🔥 REFRESH PROJECT VIEW (important)
            ProjectView.getInstance(project).refresh()

        } else {
            reset()
        }
    }

    override fun reset() {
        workingList = settings.state.parentFolders.toMutableList()
        refresh()
    }

    // =========================
    // UI refresh
    // =========================
    private fun refresh() {
        model.clear()
        workingList.forEach { model.addElement(it) }
    }
}