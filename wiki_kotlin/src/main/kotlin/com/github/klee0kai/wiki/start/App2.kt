package com.github.klee0kai.wiki.start

object App2 {
    val DI: SevenPlanetComponent = SevenPlanetComponentStoneComponent()

    fun main(args: Array<String>) {
        val earth = DI.planets().earth()
    }
}
