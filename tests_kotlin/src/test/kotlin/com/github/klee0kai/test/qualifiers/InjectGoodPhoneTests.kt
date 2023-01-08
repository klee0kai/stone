package com.github.klee0kai.test.qualifiers

import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize
import com.github.klee0kai.test.tech.phone.GoodPhone
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InjectGoodPhoneTests {
    @Test
    fun createGoodPhoneTest() {
        //When
        val goodPhone = GoodPhone(DataStorageSize("120GB"), RamSize("8GB"))
        goodPhone.buy()

        //Then
        assertEquals("120GB", goodPhone.dataStorage.size)
        assertEquals("8GB", goodPhone.ram.size)
    }

    @Test
    fun cacheInjectedPhoneTest() {
        val goodPhone1 = GoodPhone(DataStorageSize("120GB"), RamSize("8GB"))
        val goodPhone2 = GoodPhone(DataStorageSize("120GB"), RamSize("8GB"))

        //When
        goodPhone1.buy()
        goodPhone2.buy()

        //Then
        assertEquals(goodPhone1.ram.uuid, goodPhone2.ram.uuid)
    }
}