package com.github.klee0kai.tests.java_models.identifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class TechFactoryTests {
    @Test
    fun differentCreateTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val ram8Gb = DI.factory().ram(RamSize("8GB"))
        val ram8Gb2 = DI.factory().ram(RamSize("8GB"))

        //Then: created components are different
        assertNotEquals(ram8Gb.uuid, ram8Gb2.uuid)
    }

    @Test
    fun enumDifferentTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val android = DI.factory().phoneOs(PhoneOsType.Android)
        val android2 = DI.factory().phoneOs(PhoneOsType.Android)
        val osNull = DI.factory().phoneOs(null)

        //Then: created components are different
        assertNotEquals(android.uuid, osNull.uuid)
        assertNotEquals(android.uuid, android2.uuid)
    }
}