package com.github.klee0kai.tests.java_models.identifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.base_phone.PhoneComponent
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class TechComponentsTests {
    @Test
    fun differentCreateTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val ram8Gb = DI.components().ram(RamSize("8GB"))
        val ram9Gb = DI.components().ram(RamSize("9GB"))

        //Then: created components are different
        assertNotEquals(ram8Gb.uuid, ram9Gb.uuid)
    }

    @Test
    fun singleCacheTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val ram1 = DI.components().ram(RamSize("8GB"))
        val ram2 = DI.components().ram(RamSize("8GB"))

        //Then: cached ram
        assertEquals(ram1.uuid, ram2.uuid)
    }

    @Test
    fun enumDifferentTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val android = DI.components().phoneOs(PhoneOsType.Android)
        val osNull = DI.components().phoneOs(null)
        val ios = DI.components().phoneOs(PhoneOsType.Ios)

        //Then: created components are different
        assertNotEquals(android.uuid, osNull.uuid)
        assertNotEquals(android.uuid, ios.uuid)
    }

    @Test
    fun enumCachedTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

        //When
        val android = DI.components().phoneOs(PhoneOsType.Android)
        val android2 = DI.components().phoneOs(PhoneOsType.Android)

        //Then: created components are different
        assertEquals(android.uuid, android2.uuid)
    }
}