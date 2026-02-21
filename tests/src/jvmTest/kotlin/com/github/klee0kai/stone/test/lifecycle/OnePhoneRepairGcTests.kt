package com.github.klee0kai.stone.test.lifecycle

import com.github.klee0kai.stone.weakref.Memory
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.base_phone.PhoneComponentStoneComponent
import com.github.klee0kai.test.tech.phone.OnePhone
import kotlin.test.*

/**
 * Test lifecycle owner implemented in providing model
 */
class OnePhoneRepairGcTests {


    @Test
    fun onePhoneDropWatterTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val onePhone: OnePhone = OnePhone()
        DI.inject(onePhone)
        val batteryRef = WeakRef(onePhone.battery)
        val dataStorageRef = WeakRef(onePhone.dataStorage)
        val ramRef = WeakRef(onePhone.ram)

        //When
        onePhone.dropToWatter()
        Memory.gc()

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
        Memory.gc()

        //Then: Phone can not be repaired, components lost
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }


    @Test
    fun onePhoneDrownedRepairTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val onePhone: OnePhone = OnePhone()
        DI.inject(onePhone)
        val ramUuid: String = onePhone.dataStorage!!.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(10)
        Memory.gc()
        DI.inject(onePhone)

        //Then: Can be repair after little time
        assertEquals(ramUuid, onePhone.dataStorage?.uuid)
    }


    @Test
    fun onePhoneDeepDrownedRepairTest() {
        //Given
        val DI = PhoneComponentStoneComponent()
        val onePhone: OnePhone = OnePhone()
        DI.inject(onePhone)
        val ramUuid: String = onePhone.dataStorage!!.uuid

        //When
        onePhone.dropToWatter()
        Thread.sleep(120)
        Memory.gc()
        DI.inject(onePhone)

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, onePhone.dataStorage!!.uuid)
    }
}
