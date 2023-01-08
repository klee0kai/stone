package com.github.klee0kai.stone.test.lifecycle;

import com.github.klee0kai.test.tech.PhoneStore;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test.tech.phone.OnePhone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test lifecycle owner implemented in providing model
 */
public class OnePhoneRepairTests {


    @Test
    public void onePhoneInjectTest() {
        //Given
        PhoneStore.recreate();

        //When
        OnePhone onePhone = new OnePhone();
        onePhone.buy();

        //Then
        assertNotNull(onePhone.battery);
        assertNotNull(onePhone.dataStorage);
        assertNotNull(onePhone.ram);
    }

    @Test
    public void onePhoneBrokeTest() {
        //Given
        PhoneStore.recreate();
        OnePhone onePhone = new OnePhone();
        onePhone.buy();
        WeakReference<Battery> batteryRef = new WeakReference<>(onePhone.battery);
        WeakReference<DataStorage> dataStorageRef = new WeakReference<>(onePhone.dataStorage);
        WeakReference<Ram> ramRef = new WeakReference<>(onePhone.ram);

        //When
        onePhone.broke();
        System.gc();
        onePhone.repair();

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get());
        assertNull(dataStorageRef.get());
        assertNull(ramRef.get());
    }

    @Test
    public void onePhoneDropWatterTest() throws InterruptedException {
        //Given
        PhoneStore.recreate();
        OnePhone onePhone = new OnePhone();
        onePhone.buy();
        WeakReference<Battery> batteryRef = new WeakReference<>(onePhone.battery);
        WeakReference<DataStorage> dataStorageRef = new WeakReference<>(onePhone.dataStorage);
        WeakReference<Ram> ramRef = new WeakReference<>(onePhone.ram);

        //When
        onePhone.dropToWatter();
        System.gc();

        //Then: Phone not link with his components
        assertNull(onePhone.battery);
        assertNull(onePhone.dataStorage);
        assertNull(onePhone.ram);

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
    public void onePhoneDrownedRepairTest() throws InterruptedException {
        //Given
        PhoneStore.recreate();
        OnePhone onePhone = new OnePhone();
        onePhone.buy();
        UUID ramUuid = onePhone.dataStorage.uuid;

        //When
        onePhone.dropToWatter();
        Thread.sleep(10);
        System.gc();
        onePhone.repair();

        //Then: Can be repair after little time
        assertEquals(ramUuid, onePhone.dataStorage.uuid);
    }


    @Test
    public void onePhoneDeepDrownedRepairTest() throws InterruptedException {
        //Given
        PhoneStore.recreate();
        OnePhone onePhone = new OnePhone();
        onePhone.buy();
        UUID ramUuid = onePhone.dataStorage.uuid;

        //When
        onePhone.dropToWatter();
        Thread.sleep(120);
        System.gc();
        onePhone.repair();

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, onePhone.dataStorage.uuid);
    }

}
