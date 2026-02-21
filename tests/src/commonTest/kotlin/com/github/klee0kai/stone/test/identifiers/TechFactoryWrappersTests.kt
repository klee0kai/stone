package com.github.klee0kai.stone.test.identifiers

import com.github.klee0kai.test.di.techfactory.TechFactoryComponentStoneComponent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class TechFactoryWrappersTests {

    @Test
    fun lazyProvideTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val battery = DI.batteryLazy()

        //Then
        assertEquals(
            battery?.get()?.uuid,
            battery?.get()?.uuid,
        )
    }

    @Test
    fun softRefProvideTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val battery = DI.batterySoft()

        //Then
        assertNotNull(battery!!.get())
    }

    @Test
    fun weakRefProvideTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val battery = DI.batteryWeak()

        //Then
        assertNotNull(battery!!.get())
    }

    @Test
    fun phantom1ProvideTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val battery = DI.batteryPhantomProvider()

        //Then
        assertNotEquals(
            battery!!.get()!!.uuid,
            battery.get()!!.uuid,
        )
    }

    @Test
    fun phantom2ProvideTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val battery = DI.batteryProvider()

        //Then
        assertNotEquals(
            battery!!.get()!!.uuid,
            battery.get()!!.uuid,
        )
    }

    @Test
    fun phantom3ProvideTest() {
        //Given
        val DI = TechFactoryComponentStoneComponent()

        //When
        val battery = DI.batteryProviderIRef()

        //Then
        assertNotEquals(
            battery!!.get()!!.uuid,
            battery.get()!!.uuid,
        )
    }

}
