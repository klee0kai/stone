package com.github.klee0kai.wiki.init

import com.github.klee0kai.stone.Stone

fun main(args: Array<String>) {
    // init stage
    val module = FeatureModule()
    val DI = Stone.createComponent(AppComponent::class.java)
    DI.initFeatureModule(module)
    // some work
    // dynamic feature loaded
    val moduleNewFeatures = DynamicFeatureModule()
    DI.initFeatureModule(moduleNewFeatures)
}
