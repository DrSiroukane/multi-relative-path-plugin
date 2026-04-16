package com.drsiroukane.plugins.multirelativepath.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.PlatformIcons
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeSelectionModel

class IncludeFoldersDialog(private val project: Project) : DialogWrapper(project, true) {

    private val rootNode = DefaultMutableTreeNode()
    private val tree: JTree
    private val selectedPaths = mutableListOf<String>()

    init {
        title = "Select Folder"
        setOKButtonText("Done")
        setCancelButtonText("Cancel")

        val projectDir = getProjectRoot()

        if (projectDir != null) {
            val psiManager = PsiManager.getInstance(project)
            val psiProjectDir = psiManager.findDirectory(projectDir)

            if (psiProjectDir != null) {
                rootNode.userObject = projectDir
                buildTree(rootNode, psiProjectDir)
            }
        }

        tree = JTree(DefaultTreeModel(rootNode)).apply {
            isRootVisible = true
            showsRootHandles = true
            selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        }

        // ✅ Renderer
        tree.cellRenderer = object : DefaultTreeCellRenderer() {
            override fun getTreeCellRendererComponent(
                tree: JTree,
                value: Any?,
                selected: Boolean,
                expanded: Boolean,
                leaf: Boolean,
                row: Int,
                hasFocus: Boolean
            ): Component {
                super.getTreeCellRendererComponent(
                    tree, value, selected, expanded, leaf, row, hasFocus
                )

                val node = value as? DefaultMutableTreeNode
                val vf = node?.userObject as? VirtualFile

                if (vf != null) {
                    text = if (vf.parent == null) vf.path else vf.name
                    icon = when {
                        vf.isDirectory -> PlatformIcons.FOLDER_ICON
                        else -> vf.fileType.icon
                    }
                }

                return this
            }
        }

        init()
    }

    /** Get project root safely */
    private fun getProjectRoot(): VirtualFile? {
        project.guessProjectDir()?.let { return it }

        project.basePath?.let { path ->
            return LocalFileSystem.getInstance().findFileByPath(path)
        }

        return null
    }

    /** Build tree with folders only */
    private fun buildTree(parentNode: DefaultMutableTreeNode, psiDir: PsiDirectory) {
        for (subDir in psiDir.subdirectories) {
            val vf = subDir.virtualFile
            val childNode = DefaultMutableTreeNode(vf)
            parentNode.add(childNode)

            buildTree(childNode, subDir)
        }
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout()).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        }

        val scrollPane = JBScrollPane(tree).apply {
            preferredSize = Dimension(500, 450)
        }

        panel.add(scrollPane, BorderLayout.CENTER)
        return panel
    }

    /** Capture selected folder when user clicks Done */
    override fun doOKAction() {
        val selectedNode = tree.lastSelectedPathComponent as? DefaultMutableTreeNode
        val vf = selectedNode?.userObject as? VirtualFile

        vf?.path?.let {
            if (!selectedPaths.contains(it)) {
                selectedPaths.add(it)
            }
        }

        super.doOKAction()
    }

    fun getSelectedPaths(): List<String> = selectedPaths.toList()
}