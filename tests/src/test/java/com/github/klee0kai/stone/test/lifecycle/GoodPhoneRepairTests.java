package com.github.klee0kai.stone.test.lifecycle;

import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.PhoneStore;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test lifecycle owner over LifecycleUtils
 */
public class GoodPhoneRepairTests {


    @Test
    public void goodPhoneInjectTest() {
        //Given
        PhoneStore.recreate();

        //When
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("64G"), new RamSize("4G"));
        goodPhone.buy();

        //Then
        assertNotNull(goodPhone.battery);
        assertNotNull(goodPhone.dataStorage);
        assertNotNull(goodPhone.ram);
    }

    @Test
    public void goodPhoneBrokeTest() {
        //Given
        PhoneStore.recreate();
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("64G"), new RamSize("4G"));
        goodPhone.buy();
        WeakReference<Battery> batteryRef = new WeakReference<>(goodPhone.battery);
        WeakReference<DataStorage> dataStorageRef = new WeakReference<>(goodPhone.dataStorage);
        WeakReference<Ram> ramRef = new WeakReference<>(goodPhone.ram);

        //When
        goodPhone.broke();
        System.gc();
        goodPhone.repair();

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get());
        assertNull(dataStorageRef.get());
        assertNull(ramRef.get());
    }

    @Test
    public void goodPhoneDropWatterTest() throws InterruptedException {
        //Given
        PhoneStore.recreate();
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("64G"), new RamSize("4G"));
        goodPhone.buy();
        WeakReference<Battery> batteryRef = new WeakReference<>(goodPhone.battery);
        WeakReference<DataStorage> dataStorageRef = new WeakReference<>(goodPhone.dataStorage);
        WeakReference<Ram> ramRef = new WeakReference<>(goodPhone.ram);

        //When
        goodPhone.dropToWater();
        System.gc();

        //Then: Phone not link with his components
        assertNull(goodPhone.battery);
        assertNull(goodPhone.dataStorage);
        assertNull(goodPhone.ram);

        //Then: Phone components is alive little time.
        assertNotNull(batteryRef.get());
        assertNotNull(dataStorageRef.get());
        assertNotNull(ramRef.get());

        //When: After little time
        Thread.sleep(120);
        System.gc();

        //Then: Phone can not be repaired, components lost
        assertNull(batteryRef.get());
        assertNull(dataStorageRef.get());
        assertNull(ramRef.get());
    }


    @Test
    public void goodPhoneDrownedRepairTest() throws InterruptedException {
        //Given
        PhoneStore.recreate();
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("64G"), new RamSize("4G"));
        goodPhone.buy();
        UUID ramUuid = goodPhone.dataStorage.uuid;

        //When
        goodPhone.dropToWater();
        Thread.sleep(10);
        System.gc();
        goodPhone.repair();

        //Then: Can be repair after little time
        assertEquals(ramUuid, goodPhone.dataStorage.uuid);
    }


    @Test
    public void goodPhoneDeepDrownedRepairTest() throws InterruptedException {
        //Given
        PhoneStore.recreate();
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("64G"), new RamSize("4G"));
        goodPhone.buy();
        UUID ramUuid = goodPhone.dataStorage.uuid;

        //When
        goodPhone.dropToWater();
        Thread.sleep(120);
        System.gc();
        goodPhone.repair();

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, goodPhone.dataStorage.uuid);
    }

}
