package com.github.klee0kai.stone.test.lifecycle

import com.github.klee0kai.stone.weakref.Memory
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.base_phone.PhoneComponentStoneComponent
import com.github.klee0kai.test.tech.phone.OnePhone
import kotlin.test.*

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
        val batteryRef = WeakRef(onePhone.battery)
        val dataStorageRef = WeakRef(onePhone.dataStorage)
        val ramRef = WeakRef(onePhone.ram)

        //When
        onePhone.broke()
        Memory.gc()
        DI.inject(onePhone)

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }


}
