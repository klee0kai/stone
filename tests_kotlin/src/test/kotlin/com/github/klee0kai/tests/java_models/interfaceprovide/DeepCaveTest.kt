package com.github.klee0kai.tests.java_models.interfaceprovide

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.earthmirror.EarthComponent
import com.github.klee0kai.test.mowgli.earth.Cave
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
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

    @Test
    fun anyCaveTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then: No any cave constructors
        assertNull(di.east().cave(Cave.CaveType.Glacier))
        assertNull(di.west().cave(Cave.CaveType.Fracture))
    }

    @Test
    fun bigCaveTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then: No any cave constructors
        assertNull(di.east().cave(Cave.CaveType.Glacier, 2, 3))
        assertNull(di.west().cave(Cave.CaveType.Fracture, 5, 6))
    }

    @Test
    fun caveTheTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then: We not fix arg sequence
        assertNull(di.east().cave(2, Cave.CaveType.Glacier))
        assertNull(di.west().cave(3, Cave.CaveType.Fracture))
    }
}