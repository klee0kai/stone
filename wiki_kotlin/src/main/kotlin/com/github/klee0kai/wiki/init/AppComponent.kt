package com.github.klee0kai.wiki.init

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.Init

@Component
interface AppComponent {
    fun feature(): FeatureModule

    fun starsDependencies(): StarsDependencies

    fun starsDependencies2(): StarsDependencies2

    @Init
    fun initFeatureModule(featureModule: FeatureModule?)
}
