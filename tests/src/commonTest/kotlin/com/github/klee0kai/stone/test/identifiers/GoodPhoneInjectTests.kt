package com.github.klee0kai.stone.test.identifiers

import com.github.klee0kai.test.di.base_phone.PhoneComponentStoneComponent
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.phone.GoodPhone
import kotlin.test.Test
import kotlin.test.assertEquals

class GoodPhoneInjectTests {

    @Test
    fun createGoodPhoneTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()

        //When
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //Then
        assertEquals("120GB", goodPhone.dataStorage!!.size)
        assertEquals("8GB", goodPhone.ram!!.size)
    }

    @Test
    fun cacheInjectedPhoneTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone1 = GoodPhone()
        val goodPhone2 = GoodPhone()

        //When
        DI.inject(goodPhone1, goodPhone1.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))
        DI.inject(goodPhone2, goodPhone2.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //Then
        assertEquals(goodPhone1.ram!!.uuid, goodPhone2.ram!!.uuid)
    }

}
