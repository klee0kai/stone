package com.github.klee0kai.wiki.init

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ExtendOf

@Component
interface AppProComponent : AppComponent {

    override fun feature(): ProFeatureModule

    @ExtendOf
    fun extendComponent(parent: AppComponent)
}
