package com.github.klee0kai.wiki.start

import com.github.klee0kai.stone.annotations.component.Component

@Component
interface SevenPlanetComponent {

    fun planets(): SevenPlanetModule

}