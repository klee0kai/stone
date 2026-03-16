package com.dirgub.klee0kai.stone.text_ext.identifiers

import com.github.klee0kai.test.di.base_phone.PhoneComponent
import com.github.klee0kai.test.di.base_phone.PhoneComponentStoneComponent
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.phone.GoodPhone
import com.github.klee0kai.test_ext.inject.di.base_phone.PhoneExtComponent
import com.github.klee0kai.test_ext.inject.di.base_phone.PhoneExtComponentStoneComponent
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class GoodPhoneInjectExtTests {


    @Test
    @Disabled("https://github.com/klee0kai/stone/issues/42")
    fun createdIsReusableTest() {
        //Given
        val DI: PhoneComponent = PhoneComponentStoneComponent()
        val goodPhone1 = GoodPhone()
        DI.inject(goodPhone1, goodPhone1.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //When
        val DIPro: PhoneExtComponent = PhoneExtComponentStoneComponent()
        DIPro.extOf(DI)
        val goodPhone2 = GoodPhone()
        DIPro.inject(goodPhone2, goodPhone2.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //Then
        assertEquals(goodPhone1.ram!!.uuid, goodPhone2.ram!!.uuid)
        assertTrue(goodPhone1.ram is DDR3Ram)
    }


    @Test
    fun createGoodPhoneFromProtoTest() {
        //Given
        val DI: PhoneComponent = PhoneComponentStoneComponent()
        val DIPro: PhoneExtComponent = PhoneExtComponentStoneComponent()
        DIPro.extOf(DI)

        val goodPhone = GoodPhone()

        //When
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //Then
        assertEquals("120GB", goodPhone.dataStorage!!.size)
        assertEquals("8GB", goodPhone.ram!!.size)
        assertTrue(goodPhone.ram is DDR3Ram)
    }


    @Test
    fun createGoodPhoneFromProTest() {
        //Given
        val DI: PhoneComponent = PhoneComponentStoneComponent()
        val DIPro: PhoneExtComponent = PhoneExtComponentStoneComponent()
        DIPro.extOf(DI)

        val goodPhone = GoodPhone()

        //When
        DIPro.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //Then
        assertEquals("120GB", goodPhone.dataStorage!!.size)
        assertEquals("8GB", goodPhone.ram!!.size)
        assertTrue(goodPhone.ram is DDR3Ram)
    }


    @Test
    fun createGoodPhoneFromProExtTest() {
        //Given
        val DI: PhoneComponent = PhoneComponentStoneComponent()
        val DIPro: PhoneExtComponent = PhoneExtComponentStoneComponent()
        DIPro.extOf(DI)
        val goodPhone = GoodPhone()

        //When
        DIPro.injectExt(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //Then
        assertEquals("120GB", goodPhone.dataStorage!!.size)
        assertEquals("8GB", goodPhone.ram!!.size)
        assertTrue(goodPhone.ram is DDR3Ram)
    }


    @Test
    fun cacheInjectedPhoneTest() {
        //Given
        val DI: PhoneComponent = PhoneComponentStoneComponent()
        val DIPro: PhoneExtComponent = PhoneExtComponentStoneComponent()
        DIPro.extOf(DI)
        val goodPhone1 = GoodPhone()
        val goodPhone2 = GoodPhone()


        //When
        DI.inject(goodPhone1, goodPhone1.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))
        DIPro.inject(goodPhone2, goodPhone2.lifeCycleOwner, DataStorageSize("120GB"), RamSize("8GB"))

        //Then
        assertEquals(goodPhone1.ram!!.uuid, goodPhone2.ram!!.uuid)
        assertTrue(goodPhone1.ram is DDR3Ram)
    }

}
