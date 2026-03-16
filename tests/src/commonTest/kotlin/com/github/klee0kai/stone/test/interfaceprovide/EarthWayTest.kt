package com.github.klee0kai.stone.test.interfaceprovide

import com.github.klee0kai.test.di.earthmirror.EarthComponentStoneComponent
import kotlin.test.Test
import kotlin.test.assertNotNull

class EarthWayTest {

    @Test
    fun christopherColumbusTest() {
        //When
        val di = EarthComponentStoneComponent()

        //Then
        assertNotNull(di.west().mountainImp())
        assertNotNull(di.west().riverImpl())
    }

}
