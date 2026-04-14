package com.drsiroukane.plugins.multirelativepath.actions

import com.drsiroukane.plugins.multirelativepath.settings.MultiRelativePathSettings
import com.drsiroukane.plugins.multirelativepath.util.Notifier
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vfs.VirtualFile
import java.awt.datatransfer.StringSelection

class CopyMultiRelativePathAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {

        val project = e.project
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        if (file == null || project == null) {
            e.presentation.isVisible = false
            return
        }

        val settings = MultiRelativePathSettings.getInstance(project)

        // Check if file OR folder belongs to a relative parent
        val hasRelativeParent = settings.state.parentFolders.any { parent ->
            file.path.startsWith(parent)
        }

        e.presentation.isVisible = hasRelativeParent
        e.presentation.isEnabled = hasRelativeParent
    }

    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        val file: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val settings = MultiRelativePathSettings.getInstance(project)
        val parents = settings.state.parentFolders

        val results = parents.mapNotNull { parent ->

            if (file.path.startsWith(parent)) {

                val relative = file.path
                    .removePrefix(parent)
                    .removePrefix("/")

                // When clicking directly the parent folder
                if (relative.isBlank()) file.name else relative

            } else null
        }

        if (results.isEmpty()) {
            Notifier.error(project, "No relative paths found")
            return
        }

        // Auto copy when only one result
        if (results.size == 1) {

            val value = results.first()

            CopyPasteManager.getInstance()
                .setContents(StringSelection(value))

            Notifier.info(project, "Copied:\n$value")
            return
        }

        // Popup when multiple parents match
        JBPopupFactory.getInstance()
            .createPopupChooserBuilder(results)
            .setTitle("Copy Multi Relative Path")
            .setItemChosenCallback { selected ->

                CopyPasteManager.getInstance()
                    .setContents(StringSelection(selected))

                Notifier.info(project, "Copied:\n$selected")
            }
            .createPopup()
            .showInBestPositionFor(e.dataContext)
    }
}