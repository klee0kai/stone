package com.github.klee0kai.thekey.stone.ksp.target

val String.componentClName: String get() = "${this}StoneComponent"

val String.factoryClName: String get() = "${this}_FStone"

val String.moduleClName: String get() = "${this}_MStone"

val String.cacheControlClName: String get() = "${this}_CCMStone"

val String.hiddenModuleClName: String get() = "${this}_HMStone"

val String.wrapperClName: String get() = "${this}_TWStone"
