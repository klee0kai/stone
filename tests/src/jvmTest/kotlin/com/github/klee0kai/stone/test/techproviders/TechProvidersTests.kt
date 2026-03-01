package com.github.klee0kai.stone.test.techproviders

import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import com.github.klee0kai.test.di.techproviders.TechFactoryGenProvideComponentStoneComponent
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TechProvidersTests {

    @Test
    fun batteryProvideTest() {
        //Given
        val di = TechFactoryGenProvideComponentStoneComponent()

        //when
        val battery = di.battery()

        //then
        assertNotNull(battery.get()!!.uuid)
    }

    @Test
    fun phoneOsProvideTest() = runBlocking {
        //Given
        val di = TechFactoryGenProvideComponentStoneComponent()

        //when
        val phoneOs = di.phoneOs()

        //then
        assertNotNull(phoneOs.get()!!.uuid)
        assertEquals(PhoneOsType.UbuntuTouch, phoneOs.get()!!.phoneOsType)
        assertEquals(PhoneOsVersion(version = "def_version"), phoneOs.get()!!.version)
    }

}