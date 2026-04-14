package com.drsiroukane.plugins.multirelativepath.actions

import com.drsiroukane.plugins.multirelativepath.settings.MultiRelativePathSettings
import com.drsiroukane.plugins.multirelativepath.util.Notifier
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile

class SetRelativeParentAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread =
        ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {

        val file: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)

        val isFolder = file?.isDirectory == true

        if (!isFolder) {
            e.presentation.isVisible = false
            return
        }

        val project = e.project ?: return
        val settings = MultiRelativePathSettings.Companion.getInstance(project)

        val isAlreadyParent = settings.state.parentFolders.contains(file.path)

        e.presentation.isVisible = true
        e.presentation.isEnabled = true

        e.presentation.text = if (isAlreadyParent)
            "Unset Relative Parent"
        else
            "Set As Relative Parent"
    }

    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project ?: return
        val folder = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val settings = MultiRelativePathSettings.Companion.getInstance(project)
        val path = folder.path

        val isAlreadyParent = settings.state.parentFolders.contains(path)

        if (isAlreadyParent) {
            settings.state.parentFolders.remove(path)
            Notifier.info(project, "Removed relative parent:\n$path")
        } else {
            settings.state.parentFolders.add(path)
            Notifier.info(project, "Added relative parent:\n$path")
        }

        // 🔥 CRITICAL: refresh UI so decorator updates
        ProjectView.getInstance(project).refresh()
    }
}