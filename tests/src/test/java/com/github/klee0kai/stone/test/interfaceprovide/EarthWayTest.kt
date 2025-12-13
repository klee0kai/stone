package com.github.klee0kai.stone.test.interfaceprovide

import com.github.klee0kai.test.di.earthmirror.EarthComponentStoneComponent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

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
