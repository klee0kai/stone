package com.github.klee0kai.stone.test.identifiers

import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.di.techfactory.TechFactoryComponentStoneComponent
import kotlin.test.Test
import kotlin.test.assertNotEquals

class TechFactoryTests {

    @Test
    fun differentCreateTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val ram8Gb = DI.factory().ram(RamSize("8GB"))
        val ram8Gb2 = DI.factory().ram(RamSize("8GB"))

        //Then: created components are different
        assertNotEquals(ram8Gb?.uuid, ram8Gb2?.uuid)
    }


    @Test
    fun enumDifferentTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val android = DI.factory().phoneOs(PhoneOsType.Android)
        val android2 = DI.factory().phoneOs(PhoneOsType.Android)
        val osNull = DI.factory().phoneOs(null)

        //Then: created components are different
        assertNotEquals(android?.uuid, osNull?.uuid)
        assertNotEquals(android?.uuid, android2?.uuid)
    }
}
