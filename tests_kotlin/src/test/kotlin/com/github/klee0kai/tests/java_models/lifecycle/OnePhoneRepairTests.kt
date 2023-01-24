package com.github.klee0kai.tests.java_models.lifecycle

import com.github.klee0kai.test.tech.PhoneStore
import com.github.klee0kai.test.tech.phone.OnePhone
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

/**
 * Test lifecycle owner implemented in providing model
 */
class OnePhoneRepairTests {

    @Test
    fun onePhoneInjectTest() {
        //When
        PhoneStore.recreate()
        val onePhone = OnePhone()
        onePhone.buy()

        //Then
        Assertions.assertNotNull(onePhone.battery)
        Assertions.assertNotNull(onePhone.dataStorage)
        Assertions.assertNotNull(onePhone.ram)
    }

    @Test
    fun onePhoneBrokeTest() {
        //Given
        PhoneStore.recreate()
        val onePhone = OnePhone()
        onePhone.buy()
        val batteryRef = WeakReference(onePhone.battery)
        val dataStorageRef = WeakReference(onePhone.dataStorage)
        val ramRef = WeakReference(onePhone.ram)

        //When
        onePhone.broke()
        System.gc()
        onePhone.repair()

        //Then: Need new details for repair phone. Old components collected  by GC
        Assertions.assertNull(batteryRef.get())
        Assertions.assertNull(dataStorageRef.get())
        Assertions.assertNull(ramRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onePhoneDropWatterTest() {
        //Given
        PhoneStore.recreate()
        val onePhone = OnePhone()
        onePhone.buy()
        val batteryRef = WeakReference(onePhone.battery)
        val dataStorageRef = WeakReference(onePhone.dataStorage)
        val ramRef = WeakReference(onePhone.ram)

        //When
        onePhone.dropToWatter()
        System.gc()

        //Then: Phone not link with his components
        Assertions.assertNull(onePhone.battery)
        Assertions.assertNull(onePhone.dataStorage)
        Assertions.assertNull(onePhone.ram)

        //Then: Phone components is alive little time.
        Assertions.assertNotNull(batteryRef.get())
        Assertions.assertNotNull(dataStorageRef.get())
        Assertions.assertNotNull(ramRef.get())

        //When: After little time
        Thread.sleep(120)
        System.gc()

        //Then: Phone can not be repaired, components lost
        Assertions.assertNull(batteryRef.get())
        Assertions.assertNull(dataStorageRef.get())
        Assertions.assertNull(ramRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun onePhoneDrownedRepairTest() {
        //Given
        PhoneStore.recreate()
        val onePhone = OnePhone()
        onePhone.buy()
        val ramUuid = onePhone.dataStorage.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(10)
        System.gc()
        onePhone.repair()

        //Then: Can be repair after little time
        Assertions.assertEquals(ramUuid, onePhone.dataStorage.uuid)
    }

    @Test
    @Throws(InterruptedException::class)
    fun onePhoneDeepDrownedRepairTest() {
        //Given
        PhoneStore.recreate()
        val onePhone = OnePhone()
        onePhone.buy()
        val ramUuid = onePhone.dataStorage.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(120)
        System.gc()
        onePhone.repair()

        //Then: Can not be repair without new details
        Assertions.assertNotEquals(ramUuid, onePhone.dataStorage.uuid)
    }

}