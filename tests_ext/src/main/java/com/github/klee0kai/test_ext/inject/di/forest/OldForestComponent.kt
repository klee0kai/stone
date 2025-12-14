package com.github.klee0kai.test_ext.inject.di.forest

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ExtendOf
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner
import com.github.klee0kai.test.di.base_forest.ForestComponent
import com.github.klee0kai.test_ext.inject.mowgli.animal.OldHorse

@Component
interface OldForestComponent : ForestComponent {

    override fun identity(): OldIdentityModule

    @ExtendOf
    fun extOf(parent: ForestComponent)

    fun diseases(): DiseasesModule?

    fun inject(horse: OldHorse?, stoneLifeCycleOwner: StoneLifeCycleOwner?)

    fun inject(horse: OldHorse?)

}
