package com.github.klee0kai.tests.java_models.qualifiers

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.types.wrappers.IRef
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent
import com.github.klee0kai.test.tech.components.Battery
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.Reference

class TechFactoryWrappersTests {
    @Test
    fun lazyProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val battery: IRef<Battery> = DI.batteryLazy()

        //Then
        assertEquals(
            battery.get().uuid,
            battery.get().uuid
        )
    }

    @Test
    fun softRefProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val battery: Reference<Battery> = DI.batterySoft()

        //Then
        assertNotNull(battery.get())
    }

    @Test
    fun weakRefProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val battery: Reference<Battery> = DI.batteryWeak()

        //Then
        assertNotNull(battery.get())
    }

    @Test
    fun phantom1ProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val battery: IRef<Battery> = DI.batteryPhantomProvider()

        //Then
        assertNotEquals(
            battery.get().uuid,
            battery.get().uuid
        )
    }

    @Test
    fun phantom2ProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val battery = DI.batteryProvider()

        //Then
        assertNotEquals(
            battery.get().uuid,
            battery.get().uuid
        )
    }

    @Test
    fun phantom3ProvideTest() {
        //Given
        val DI = Stone.createComponent(TechFactoryComponent::class.java)

        //When
        val battery = DI.batteryProviderIRef()

        //Then
        assertNotEquals(
            battery.get().uuid,
            battery.get().uuid
        )
    }
}