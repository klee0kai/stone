package com.github.klee0kai.test_ext.inject.di.bindinstance.simple

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ExtendOf
import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponent

@Component
interface GodRebrandingComponent : GodWorkspaceComponent {

    override fun sunSystem(): SunSystemRebrandingModule

    @ExtendOf
    fun extendComponent(parent: GodWorkspaceComponent)

}
