package com.github.klee0kai.tests.java_models.identifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TechFactoryComponentTests {
    @Test
    fun nonArgProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val ram = DI.ram()

        //Then
        assertEquals("default", ram.size)
    }

    @Test
    fun singleArgProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val ram = DI.ram(RamSize("4G"))

        //Then
        assertEquals("4G", ram.size)
    }

    @Test
    fun nullGenerateArgProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val os = DI.phoneOs()

        //Then: should pass null missing args
        assertNull(os.phoneOsType) // missing args
        assertEquals("default", os.version.version) // default from constructor
    }

    @Test
    fun coupleArgsProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val os = DI.phoneOs(PhoneOsType.Ios, PhoneOsVersion("11"))

        //Then
        assertEquals(PhoneOsType.Ios, os.phoneOsType)
        assertEquals("11", os.version.version)
    }

    @Test
    fun differentCreateTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val ram8Gb = DI.ram(RamSize("8GB"))
        val ram8Gb2 = DI.ram(RamSize("8GB"))

        //Then: created components are different
        assertEquals("8GB", ram8Gb.size)
        assertNotEquals(ram8Gb.uuid, ram8Gb2.uuid)
    }

    @Test
    fun enumDifferentTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val android = DI.phoneOs(PhoneOsType.Android)
        val android2 = DI.phoneOs(PhoneOsType.Android)
        val osNull = DI.phoneOs(null)

        //Then: created components are different
        assertNotEquals(android.uuid, osNull.uuid)
        assertNotEquals(android.uuid, android2.uuid)
    }
}