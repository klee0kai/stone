package com.github.klee0kai.stone.test.lifecycle

import com.github.klee0kai.test.di.base_phone.PhoneComponentStoneComponent
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.phone.GoodPhone
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

/**
 * Test lifecycle owner over LifecycleUtils
 */
class GoodPhoneRepairTests {

    @Test
    fun goodPhoneInjectTest() {
        //Given
        val DI = PhoneComponentStoneComponent()

        //When buy
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))

        //Then
        assertNotNull(goodPhone.battery)
        assertNotNull(goodPhone.dataStorage)
        assertNotNull(goodPhone.ram)
    }

    @Test
    fun goodPhoneBrokeTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))
        val batteryRef = WeakReference(goodPhone.battery)
        val dataStorageRef = WeakReference(goodPhone.dataStorage)
        val ramRef = WeakReference(goodPhone.ram)

        //When broke and repair
        goodPhone.broke()
        System.gc()
        DI.inject(goodPhone, DataStorageSize("64G"), RamSize("4G"))

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun goodPhoneDropWatterTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))
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
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))
        val ramUuid = goodPhone.dataStorage!!.uuid

        //When
        goodPhone.dropToWater()
        Thread.sleep(10)
        System.gc()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))

        //Then: Can be repair after little time
        assertEquals(ramUuid, goodPhone.dataStorage!!.uuid)
    }


    @Test
    @Throws(InterruptedException::class)
    fun goodPhoneDeepDrownedRepairTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))
        val ramUuid = goodPhone.dataStorage!!.uuid

        //When
        goodPhone.dropToWater()
        Thread.sleep(120)
        System.gc()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, goodPhone.dataStorage!!.uuid)
    }

}
