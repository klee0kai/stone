package com.github.klee0kai.stone.test.lifecycle;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.DataStorage;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test.tech.phone.OnePhone;
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
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        OnePhone onePhone = new OnePhone();
        DI.inject(onePhone);

        //Then
        assertNotNull(onePhone.battery);
        assertNotNull(onePhone.dataStorage);
        assertNotNull(onePhone.ram);
    }

    @Test
    public void onePhoneBrokeTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        OnePhone onePhone = new OnePhone();
        DI.inject(onePhone);
        WeakReference<Battery> batteryRef = new WeakReference<>(onePhone.battery);
        WeakReference<DataStorage> dataStorageRef = new WeakReference<>(onePhone.dataStorage);
        WeakReference<Ram> ramRef = new WeakReference<>(onePhone.ram);

        //When
        onePhone.broke();
        System.gc();
        DI.inject(onePhone);

        //Then: Need new details for repair phone. Old components collected  by GC
        assertNull(batteryRef.get());
        assertNull(dataStorageRef.get());
        assertNull(ramRef.get());
    }

    @Test
    public void onePhoneDropWatterTest() throws InterruptedException {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        OnePhone onePhone = new OnePhone();
        DI.inject(onePhone);
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
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        OnePhone onePhone = new OnePhone();
        DI.inject(onePhone);
        UUID ramUuid = onePhone.dataStorage.uuid;

        //When
        onePhone.dropToWatter();
        Thread.sleep(10);
        System.gc();
        DI.inject(onePhone);

        //Then: Can be repair after little time
        assertEquals(ramUuid, onePhone.dataStorage.uuid);
    }


    @Test
    public void onePhoneDeepDrownedRepairTest() throws InterruptedException {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        OnePhone onePhone = new OnePhone();
        DI.inject(onePhone);
        UUID ramUuid = onePhone.dataStorage.uuid;

        //When
        onePhone.dropToWatter();
        Thread.sleep(120);
        System.gc();
        DI.inject(onePhone);

        //Then: Can not be repair without new details
        assertNotEquals(ramUuid, onePhone.dataStorage.uuid);
    }

}
