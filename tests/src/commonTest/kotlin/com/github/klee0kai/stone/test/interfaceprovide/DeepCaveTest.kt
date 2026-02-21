package com.github.klee0kai.stone.test.interfaceprovide

import com.github.klee0kai.test.di.earthmirror.EarthComponentStoneComponent
import com.github.klee0kai.test.mowgli.earth.Cave
import kotlin.test.Test
import kotlin.test.assertNotNull

class DeepCaveTest {

    @Test
    fun justCaveTest() {
        //When
        val di = EarthComponentStoneComponent()

        //Then: Cave simple constructor is available
        assertNotNull(di.east().cave())
        assertNotNull(di.west().cave())
    }

    @Test
    fun theCaveTest() {
        //When
        val di = EarthComponentStoneComponent()

        //Then: Cave 2 params constructor is available
        assertNotNull(di.east().cave(Cave.CaveType.Glacier, 2))
        assertNotNull(di.west().cave(Cave.CaveType.Fracture, 6))
    }

}
