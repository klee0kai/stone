package com.github.klee0kai.tests.java_models.interfaceprovide

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.earthmirror.EarthComponent
import com.github.klee0kai.test.mowgli.earth.Cave
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class DeepCaveTest {
    @Test
    fun justCaveTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then: Cave simple constructor is available
        assertNotNull(di.east().cave())
        assertNotNull(di.west().cave())
    }

    @Test
    fun theCaveTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then: Cave 2 params constructor is available
        assertNotNull(di.east().cave(Cave.CaveType.Glacier, 2))
        assertNotNull(di.west().cave(Cave.CaveType.Fracture, 6))
    }

}