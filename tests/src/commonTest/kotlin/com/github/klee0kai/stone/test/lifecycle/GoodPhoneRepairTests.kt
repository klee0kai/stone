package com.github.klee0kai.stone.test.lifecycle

import com.github.klee0kai.stone.weakref.Memory
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.base_phone.PhoneComponentStoneComponent
import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.phone.GoodPhone
import kotlin.test.*

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
        val batteryRef = WeakRef(goodPhone.battery)
        val dataStorageRef = WeakRef(goodPhone.dataStorage)
        val ramRef = WeakRef(goodPhone.ram)

        //When broke and repair
        goodPhone.broke()
        Memory.gc()
        DI.inject(goodPhone, DataStorageSize("64G"), RamSize("4G"))

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }

    @Test
//    @Throws(InterruptedException::class)
    fun goodPhoneDropWatterTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))
        val batteryRef = WeakRef(goodPhone.battery)
        val dataStorageRef = WeakRef(goodPhone.dataStorage)
        val ramRef = WeakRef(goodPhone.ram)

        //When
        goodPhone.dropToWater()
        Memory.gc()

        //Then: Phone not link with his components
        assertNull(goodPhone.battery)
        assertNull(goodPhone.dataStorage)
        assertNull(goodPhone.ram)

        //Then: Phone components is alive little time.
        assertNotNull(batteryRef.get())
        assertNotNull(dataStorageRef.get())
        assertNotNull(ramRef.get())

        //When: After little time
//        Thread.sleep(120)
        Memory.gc()

        //Then: Phone can not be repaired, components lost
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }


    @Test
//    @Throws(InterruptedException::class)
    fun goodPhoneDrownedRepairTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))
        val ramUuid = goodPhone.dataStorage!!.uuid

        //When
        goodPhone.dropToWater()
//        Thread.sleep(10)
        Memory.gc()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))

        //Then: Can be repair after little time
        assertEquals(ramUuid, goodPhone.dataStorage!!.uuid)
    }


    @Test
//    @Throws(InterruptedException::class)
    fun goodPhoneDeepDrownedRepairTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val goodPhone = GoodPhone()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))
        val ramUuid = goodPhone.dataStorage!!.uuid

        //When
        goodPhone.dropToWater()
//        Thread.sleep(120)
        Memory.gc()
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, DataStorageSize("64G"), RamSize("4G"))

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, goodPhone.dataStorage!!.uuid)
    }

}
