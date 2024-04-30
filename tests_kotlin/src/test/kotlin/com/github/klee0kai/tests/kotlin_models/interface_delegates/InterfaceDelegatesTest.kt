package com.github.klee0kai.tests.kotlin_models.interface_delegates

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.identifiers.MonitorSize
import com.github.klee0kai.test_kotlin.di.interface_delegates.InterfaceDelegatesComponent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InterfaceDelegatesTest {

    @Test
    fun provideKeyboardTest() {
        //Given
        val DI = Stone.createComponent(InterfaceDelegatesComponent::class.java)

        //When
        val keyboard1 = DI.factory().keyboard()
        val keyboard2 = DI.keyBoard()

        //Then
        assertNotNull(keyboard1)
        assertNotNull(keyboard2)
        assertNotEquals(
            keyboard1.uuid,
            keyboard2.uuid
        )
        assertEquals(KConnectType.Bluetooth, keyboard1.KConnectType)
        assertEquals(KConnectType.Bluetooth, keyboard2.KConnectType)
    }


    @Test
    fun provideMonitorTest() {
        //Given
        val DI = Stone.createComponent(InterfaceDelegatesComponent::class.java)

        //When
        val monitor = DI.factory().monitor(MonitorSize("2"))

        //Then
        assertNotNull(monitor)
        assertEquals("2", monitor.size.size)
        assertEquals("samsung", monitor.company.companyName)
    }

    @Test
    fun provideMouseTest() {
        //Given
        val DI = Stone.createComponent(InterfaceDelegatesComponent::class.java)

        //When
        val mouse1 = DI.factory().mouse()
        val mouse2 = DI.mouse()

        //Then
        assertNotNull(mouse1)
        assertNotNull(mouse2)
        assertNotEquals(
            mouse1.uuid,
            mouse2.uuid
        )
    }


}