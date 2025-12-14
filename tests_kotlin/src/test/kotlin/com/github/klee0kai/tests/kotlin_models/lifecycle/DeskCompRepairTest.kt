package com.github.klee0kai.tests.kotlin_models.lifecycle

import com.github.klee0kai.test_kotlin.tech.ComputerStore
import com.github.klee0kai.test_kotlin.tech.comp.DesktopComp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

/**
 * Test lifecycle owner implemented in providing model
 */
class DeskCompRepairTest {

    @Test
    fun deskCompInjectTest() {
        //When
        ComputerStore.recreate()
        val desktopComp = DesktopComp()
        desktopComp.buy()

        //Then
        assertNotNull(desktopComp.monitor)
        assertNotNull(desktopComp.keyboard)
    }


    @Test
    fun deskCompBrokeTest() {
        //Given
        ComputerStore.recreate()
        val desktopComp = DesktopComp()
        desktopComp.buy()
        val monitorRef = WeakReference(desktopComp.monitor)
        val keyboardRef = WeakReference(desktopComp.keyboard)

        //When
        desktopComp.broke()
        System.gc()
        desktopComp.repair()

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(monitorRef.get())
        assertNull(keyboardRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun deskCompDropWatterTest() {
        //Given
        ComputerStore.recreate()
        val desktopComp = DesktopComp()
        desktopComp.buy()
        val monitorRef = WeakReference(desktopComp.monitor)
        val keyboardRef = WeakReference(desktopComp.keyboard)

        //When
        desktopComp.dropToWater()
        System.gc()

        //Then: Phone not link with his components
        assertNull(desktopComp.monitor)
        assertNull(desktopComp.keyboard)

        //Then: Phone components is alive little time.
        assertNotNull(monitorRef.get())
        assertNotNull(keyboardRef.get())

        //When: After little time
        Thread.sleep(120)
        System.gc()

        //Then: Phone can not be repaired, components lost
        assertNull(monitorRef.get())
        assertNull(keyboardRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun deskCompDrownedRepairTest() {
        //Given
        ComputerStore.recreate()
        val desktopComp = DesktopComp()
        desktopComp.buy()
        val monitorUuid = desktopComp.monitor!!.uuid

        //When
        desktopComp.dropToWater()
        Thread.sleep(10)
        System.gc()
        desktopComp.repair()

        //Then: Can be repair after little time
        assertEquals(monitorUuid, desktopComp.monitor?.uuid)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deskCompDeepDrownedRepairTest() {
        //Given
        ComputerStore.recreate()
        val desktopComp = DesktopComp()
        desktopComp.buy()
        val monitorUuid = desktopComp.monitor!!.uuid

        //When
        desktopComp.dropToWater()
        Thread.sleep(120)
        System.gc()
        desktopComp.repair()

        //Then: Can be repair after little time
        assertNotEquals(monitorUuid, desktopComp.monitor?.uuid)
    }
}