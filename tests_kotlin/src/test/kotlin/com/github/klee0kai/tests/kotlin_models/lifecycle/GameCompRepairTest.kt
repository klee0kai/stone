package com.github.klee0kai.tests.kotlin_models.lifecycle

import com.github.klee0kai.test_kotlin.tech.ComputerStore
import com.github.klee0kai.test_kotlin.tech.comp.GameComp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

/**
 * Test lifecycle owner over LifecycleUtils
 */
class GameCompRepairTest {

    @Test
    fun gameCompInjectTest() {
        //When
        ComputerStore.recreate()
        val gameComp = GameComp()
        gameComp.buy()

        //Then
        assertNotNull(gameComp.monitor)
        assertNotNull(gameComp.keyboard)
    }


    @Test
    fun gameCompBrokeTest() {
        //Given
        ComputerStore.recreate()
        val gameComp = GameComp()
        gameComp.buy()
        val monitorRef = WeakReference(gameComp.monitor)
        val keyboardRef = WeakReference(gameComp.keyboard)

        //When
        gameComp.broke()
        System.gc()
        gameComp.repair()

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(monitorRef.get())
        assertNull(keyboardRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun gameCompDropWatterTest() {
        //Given
        ComputerStore.recreate()
        val gameComp = GameComp()
        gameComp.buy()
        val monitorRef = WeakReference(gameComp.monitor)
        val keyboardRef = WeakReference(gameComp.keyboard)

        //When
        gameComp.dropToWater()
        System.gc()

        //Then: Phone not link with his components
        assertNull(gameComp.monitor)
        assertNull(gameComp.keyboard)

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
    fun gameCompDrownedRepairTest() {
        //Given
        ComputerStore.recreate()
        val gameComp = GameComp()
        gameComp.buy()
        val monitorUuid = gameComp.monitor!!.uuid

        //When
        gameComp.dropToWater()
        Thread.sleep(10)
        System.gc()
        gameComp.repair()

        //Then: Can be repair after little time
        assertEquals(monitorUuid, gameComp.monitor?.uuid)
    }

    @Test
    @Throws(InterruptedException::class)
    fun gameCompDeepDrownedRepairTest() {
        //Given
        ComputerStore.recreate()
        val gameComp = GameComp()
        gameComp.buy()
        val monitorUuid = gameComp.monitor!!.uuid

        //When
        gameComp.dropToWater()
        Thread.sleep(120)
        System.gc()
        gameComp.repair()

        //Then: Can be repair after little time
        assertNotEquals(monitorUuid, gameComp.monitor?.uuid)
    }
}