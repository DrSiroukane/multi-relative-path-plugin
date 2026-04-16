package com.drsiroukane.plugins.multirelativepath.ui

import com.drsiroukane.plugins.multirelativepath.settings.MultiRelativePathSettings
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.ide.projectView.PresentationData
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.SimpleTextAttributes

class RelativeParentDecorator : ProjectViewNodeDecorator {

    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {

        val project: Project = node.project ?: return
        val file: VirtualFile = node.virtualFile ?: return

        if (!file.isDirectory) return

        val settings = MultiRelativePathSettings.getInstance(project)

        val isParent = settings.state.parentFolders.contains(file.path)

        if (isParent) {
            data.addText(
                data.presentableText + " [REL]",
                SimpleTextAttributes.GRAYED_BOLD_ATTRIBUTES
            )
        }
    }
}