package com.github.klee0kai.tests.java_models.lifecycle

import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize
import com.github.klee0kai.test.tech.PhoneStore
import com.github.klee0kai.test.tech.phone.GoodPhone
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

/**
 * Test lifecycle owner over LifecycleUtils
 */
class GoodPhoneRepairTests {

    @Test
    fun goodPhoneInjectTest() {
        //When
        PhoneStore.recreate()
        val goodPhone = GoodPhone(DataStorageSize("64G"), RamSize("4G"))
        goodPhone.buy()

        //Then
        assertNotNull(goodPhone.battery)
        assertNotNull(goodPhone.dataStorage)
        assertNotNull(goodPhone.ram)
    }

    @Test
    fun goodPhoneBrokeTest() {
        //Given
        PhoneStore.recreate()
        val goodPhone = GoodPhone(DataStorageSize("64G"), RamSize("4G"))
        goodPhone.buy()
        val batteryRef = WeakReference(goodPhone.battery)
        val dataStorageRef = WeakReference(goodPhone.dataStorage)
        val ramRef = WeakReference(goodPhone.ram)

        //When
        goodPhone.broke()
        System.gc()
        goodPhone.repair()

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun goodPhoneDropWatterTest() {
        //Given
        PhoneStore.recreate()
        val goodPhone = GoodPhone(DataStorageSize("64G"), RamSize("4G"))
        goodPhone.buy()
        val batteryRef = WeakReference(goodPhone.battery)
        val dataStorageRef = WeakReference(goodPhone.dataStorage)
        val ramRef = WeakReference(goodPhone.ram)

        //When
        goodPhone.dropToWater()
        System.gc()

        //Then: Phone not link with his components
        assertNull(goodPhone.battery)
        assertNull(goodPhone.dataStorage)
        assertNull(goodPhone.ram)

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
    fun goodPhoneDrownedRepairTest() {
        //Given
        PhoneStore.recreate()
        val goodPhone = GoodPhone(DataStorageSize("64G"), RamSize("4G"))
        goodPhone.buy()
        val ramUuid = goodPhone.dataStorage.uuid

        //When
        goodPhone.dropToWater()
        Thread.sleep(10)
        System.gc()
        goodPhone.repair()

        //Then: Can be repair after little time
        assertEquals(ramUuid, goodPhone.dataStorage.uuid)
    }

    @Test
    @Throws(InterruptedException::class)
    fun goodPhoneDeepDrownedRepairTest() {
        //Given
        PhoneStore.recreate()
        val goodPhone = GoodPhone(DataStorageSize("64G"), RamSize("4G"))
        goodPhone.buy()
        val ramUuid = goodPhone.dataStorage.uuid

        //When
        goodPhone.dropToWater()
        Thread.sleep(120)
        System.gc()
        goodPhone.repair()

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, goodPhone.dataStorage.uuid)
    }


}