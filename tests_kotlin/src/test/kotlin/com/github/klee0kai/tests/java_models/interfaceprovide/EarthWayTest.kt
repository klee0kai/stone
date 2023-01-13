package com.github.klee0kai.tests.java_models.interfaceprovide

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.earthmirror.EarthComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class EarthWayTest {
    @Test
    fun nothingInterfaceTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then
        assertNull(di.east().mountain())
        assertNull(di.east().river())
    }

    @Test
    fun nothingAbstractTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then
        assertNull(di.west().mountain())
        assertNull(di.west().river())
    }

    @Test
    fun ChristopherColumbusTest() {
        //When
        val di = Stone.createComponent(EarthComponent::class.java)

        //Then
        assertNotNull(di.west().mountainImp())
        assertNotNull(di.west().riverImpl())
    }
}