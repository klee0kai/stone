package com.github.klee0kai.test.lifecycle

import com.github.klee0kai.test.lifecycle.di.qualifier.DataStorageSize
import com.github.klee0kai.test.lifecycle.di.qualifier.RamSize
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class LifecycleTests {
    @Test
    @Throws(InterruptedException::class)
    fun onePhoneTest() {
        val onePhone = OnePhone()
        onePhone.buy()

        assertNotNull(onePhone.battery)
        assertNotNull(onePhone.dataStorage)
        assertNotNull(onePhone.ram)

        //check simple broke
        var batteryRef = WeakReference(onePhone.battery)
        var dataStorageRef = WeakReference(onePhone.dataStorage)
        var ramRef = WeakReference(onePhone.ram)

        onePhone.broke()
        System.gc()
        onePhone.repair()

        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())

        //check drown (little alive time)
        batteryRef = WeakReference(onePhone.battery)
        dataStorageRef = WeakReference(onePhone.dataStorage)
        ramRef = WeakReference(onePhone.ram)

        assertNotNull(batteryRef.get())
        assertNotNull(dataStorageRef.get())
        assertNotNull(ramRef.get())

        onePhone.drown()
        System.gc()
        assertNotNull(batteryRef.get())
        assertNotNull(dataStorageRef.get())
        assertNotNull(ramRef.get())
        assertNull(onePhone.battery)
        assertNull(onePhone.dataStorage)
        assertNull(onePhone.ram)


        Thread.sleep(120)
        System.gc()
        assertNull(batteryRef.get())
        assertNull(dataStorageRef.get())
        assertNull(ramRef.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun goodPhoneTest() {
        val goodPhone = GoodPhone(DataStorageSize("120GB"), RamSize("8GB"))
        goodPhone.buy()

        assertNotNull(goodPhone.battery)
        assertNotNull(goodPhone.dataStorage)
        assertNotNull(goodPhone.ram)
        assertEquals("120GB", goodPhone.dataStorage!!.size)
        assertEquals("8GB", goodPhone.ram!!.size)

        val ramUuid = goodPhone.dataStorage!!.uuid
        goodPhone.drown()
        Thread.sleep(10)
        System.gc()
        goodPhone.repair()
        assertEquals(ramUuid, goodPhone.dataStorage!!.uuid)


        goodPhone.drown()
        Thread.sleep(120)
        System.gc()
        goodPhone.repair()
        assertNotEquals(ramUuid, goodPhone.dataStorage!!.uuid)
    }
}