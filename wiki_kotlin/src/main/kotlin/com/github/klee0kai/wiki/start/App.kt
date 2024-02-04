package com.github.klee0kai.wiki.start

import com.github.klee0kai.stone.Stone


object App {

    val DI: SevenPlanetComponent = Stone.createComponent(SevenPlanetComponent::class.java)

    fun main(args: Array<String>) {
        val earth = DI.planets().earth()
    }
}
