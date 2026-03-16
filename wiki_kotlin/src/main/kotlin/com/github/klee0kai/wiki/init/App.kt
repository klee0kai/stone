package com.github.klee0kai.wiki.init


fun main(args: Array<String>) {
    // init stage
    val module = FeatureModule()
    val DI: AppComponent = AppComponentStoneComponent()
    DI.initFeatureModule(module)
    // some work
    // dynamic feature loaded
    val moduleNewFeatures = DynamicFeatureModule()
    DI.initFeatureModule(moduleNewFeatures)
}
