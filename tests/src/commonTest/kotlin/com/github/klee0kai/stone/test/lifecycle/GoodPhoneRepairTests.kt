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

}
