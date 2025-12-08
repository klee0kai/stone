package com.github.klee0kai.test.mowgli

import com.github.klee0kai.test.mowgli.galaxy.*
import javax.inject.Inject

class MoonSky {
    @JvmField
    @Inject
    var star: IStar? = null

    @JvmField
    @Inject
    var sun: Sun? = null


    @JvmField
    @Inject
    var planet: IPlanet? = null

    @JvmField
    @Inject
    var mercury: Mercury? = null

    @JvmField
    @Inject
    var earth: Earth? = null
}
