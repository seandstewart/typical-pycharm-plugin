package com.seandstewart.typical

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "TypicalConfigService", storages = [Storage("typical.xml")])
class TypicalConfigService : PersistentStateComponent<TypicalConfigService> {
    var initTyped = true

    override fun getState(): TypicalConfigService {
        return this
    }

    override fun loadState(config: TypicalConfigService) {
        XmlSerializerUtil.copyBean(config, this)
    }

    companion object {
        fun getInstance(project: Project?): TypicalConfigService {
            return ServiceManager.getService(project!!, TypicalConfigService::class.java)
        }
    }

}