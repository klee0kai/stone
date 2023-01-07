package com.github.klee0kai.stone.test.lifecycle;

import com.github.klee0kai.test.tech.phone.GoodPhone;
import com.github.klee0kai.test.tech.phone.OnePhone;
import com.github.klee0kai.test.lifecycle.di.qualifiers.DataStorageSize;
import com.github.klee0kai.test.lifecycle.di.qualifiers.RamSize;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class LifecycleTests {


    @Test
    public void onePhoneTest() throws InterruptedException {
        OnePhone onePhone = new OnePhone();
        onePhone.buy();

        assertNotNull(onePhone.battery);
        assertNotNull(onePhone.dataStorage);
        assertNotNull(onePhone.ram);

        //check simple broke
        WeakReference<Battery> batteryRef = new WeakReference<>(onePhone.battery);
        WeakReference<DataStorage> dataStorageRef = new WeakReference<>(onePhone.dataStorage);
        WeakReference<Ram> ramRef = new WeakReference<>(onePhone.ram);

        onePhone.broke();
        System.gc();
        onePhone.repair();


        assertNull(batteryRef.get());
        assertNull(dataStorageRef.get());
        assertNull(ramRef.get());

        //check drown (little alive time)
        batteryRef = new WeakReference<>(onePhone.battery);
        dataStorageRef = new WeakReference<>(onePhone.dataStorage);
        ramRef = new WeakReference<>(onePhone.ram);

        assertNotNull(batteryRef.get());
        assertNotNull(dataStorageRef.get());
        assertNotNull(ramRef.get());

        onePhone.drown();
        System.gc();

        assertNotNull(batteryRef.get());
        assertNotNull(dataStorageRef.get());
        assertNotNull(ramRef.get());
        assertNull(onePhone.battery);
        assertNull(onePhone.dataStorage);
        assertNull(onePhone.ram);

        Thread.sleep(120);

        System.gc();

        assertNull(batteryRef.get());
        assertNull(dataStorageRef.get());
        assertNull(ramRef.get());
    }

    @Test
    public void goodPhoneTest() throws InterruptedException {
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("120GB"), new RamSize("8GB"));
        goodPhone.buy();


        assertNotNull(goodPhone.battery);
        assertNotNull(goodPhone.dataStorage);
        assertNotNull(goodPhone.ram);

        assertEquals("120GB", goodPhone.dataStorage.size);
        assertEquals("8GB", goodPhone.ram.size);

        UUID ramUuid = goodPhone.dataStorage.uuid;


        goodPhone.drown();
        Thread.sleep(10);
        System.gc();
        goodPhone.repair();
        assertEquals(ramUuid, goodPhone.dataStorage.uuid);


        goodPhone.drown();
        Thread.sleep(120);
        System.gc();
        goodPhone.repair();
        assertNotEquals(ramUuid, goodPhone.dataStorage.uuid);

    }
}
