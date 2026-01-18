package com.github.klee0kai.stone.test.lifecycle

import com.github.klee0kai.test.di.base_phone.PhoneComponentStoneComponent
import com.github.klee0kai.test.tech.phone.OnePhone
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference
import java.util.*

/**
 * Test lifecycle owner implemented in providing model
 */
class OnePhoneRepairTests {

    @Test
    fun onePhoneInjectTest() {
        //Given
        val DI = PhoneComponentStoneComponent()

        //When
        val onePhone = OnePhone()
        DI.inject(onePhone)

        //Then
        assertNotNull(onePhone.battery)
        assertNotNull(onePhone.dataStorage)
        assertNotNull(onePhone.ram)
    }

    @Test
    fun onePhoneBrokeTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val onePhone: OnePhone = OnePhone()
        DI.inject(onePhone)
        val batteryRef = WeakReference(onePhone.battery)
        val dataStorageRef = WeakReference(onePhone.dataStorage)
        val ramRef = WeakReference(onePhone.ram)

        //When
        onePhone.broke()
        System.gc()
        DI.inject(onePhone)

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onePhoneDropWatterTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val onePhone: OnePhone = OnePhone()
        DI.inject(onePhone)
        val batteryRef = WeakReference(onePhone.battery)
        val dataStorageRef = WeakReference(onePhone.dataStorage)
        val ramRef = WeakReference(onePhone.ram)

        //When
        onePhone.dropToWatter()
        System.gc()

        //Then: Phone not link with his components
        assertNull(onePhone.battery)
        assertNull(onePhone.dataStorage)
        assertNull(onePhone.ram)

        //Then: Phone components is alive little time.
        assertNotNull(batteryRef.get())
        assertNotNull(dataStorageRef.get())
        assertNotNull(ramRef.get())

        //When: After little time
        Thread.sleep(120)
        System.gc()

        //Then: Phone can not be repaired, components lost
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }


    @Test
    @Throws(InterruptedException::class)
    fun onePhoneDrownedRepairTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val onePhone: OnePhone = OnePhone()
        DI.inject(onePhone)
        val ramUuid: UUID = onePhone.dataStorage!!.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(10)
        System.gc()
        DI.inject(onePhone)

        //Then: Can be repair after little time
        assertEquals(ramUuid, onePhone.dataStorage?.uuid)
    }


    @Test
    @Throws(InterruptedException::class)
    fun onePhoneDeepDrownedRepairTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val onePhone: OnePhone = OnePhone()
        DI.inject(onePhone)
        val ramUuid: UUID = onePhone.dataStorage!!.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(120)
        System.gc()
        DI.inject(onePhone)

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, onePhone.dataStorage!!.uuid)
    }
}
