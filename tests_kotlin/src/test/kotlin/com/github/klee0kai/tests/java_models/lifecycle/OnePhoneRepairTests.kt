package com.github.klee0kai.tests.java_models.lifecycle

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.base_phone.PhoneComponent
import com.github.klee0kai.test.tech.phone.OnePhone
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

/**
 * Test lifecycle owner implemented in providing model
 */
class OnePhoneRepairTests {
    @Test
    fun onePhoneInjectTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)

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
        val DI = Stone.createComponent(PhoneComponent::class.java)
        val onePhone = OnePhone()
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
        val DI = Stone.createComponent(PhoneComponent::class.java)
        val onePhone = OnePhone()
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
        val DI = Stone.createComponent(PhoneComponent::class.java)
        val onePhone = OnePhone()
        DI.inject(onePhone)
        val ramUuid = onePhone.dataStorage.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(10)
        System.gc()
        DI.inject(onePhone)

        //Then: Can be repair after little time
        assertEquals(ramUuid, onePhone.dataStorage.uuid)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onePhoneDeepDrownedRepairTest() {
        //Given
        val DI = Stone.createComponent(PhoneComponent::class.java)
        val onePhone = OnePhone()
        DI.inject(onePhone)
        val ramUuid = onePhone.dataStorage.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(120)
        System.gc()
        DI.inject(onePhone)

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, onePhone.dataStorage.uuid)
    }
}