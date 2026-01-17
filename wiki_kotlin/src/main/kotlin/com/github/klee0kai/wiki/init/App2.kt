package com.github.klee0kai.wiki.init

fun main(args: Array<String>) {
    val DI = AppComponentStoneComponent()
    val DIPro = AppProComponentStoneComponent()
    DIPro.extendComponent(DI)
}
