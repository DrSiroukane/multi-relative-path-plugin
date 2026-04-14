package com.drsiroukane.plugins.multirelativepath.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
    name = "MultiRelativePathSettings",
    storages = [Storage("multiRelativePath.xml")]
)
@Service(Service.Level.PROJECT)
class MultiRelativePathSettings : PersistentStateComponent<MultiRelativePathSettings.State> {

    data class State(
        var parentFolders: MutableList<String> = mutableListOf()
    )

    private var state = State()

    override fun getState(): State {
        return state
    }

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(project: Project): MultiRelativePathSettings {
            return project.getService(MultiRelativePathSettings::class.java)
        }
    }
}