package com.github.klee0kai.stone.test.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoodPhoneInjectTests {


    @Test
    public void createGoodPhoneTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        GoodPhone goodPhone = new GoodPhone();

        //When
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //Then
        assertEquals("120GB", goodPhone.dataStorage.size);
        assertEquals("8GB", goodPhone.ram.size);
    }

    @Test
    public void cacheInjectedPhoneTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        GoodPhone goodPhone1 = new GoodPhone();
        GoodPhone goodPhone2 = new GoodPhone();

        //When
        DI.inject(goodPhone1, goodPhone1.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));
        DI.inject(goodPhone2, goodPhone2.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //Then
        assertEquals(goodPhone1.ram.uuid, goodPhone2.ram.uuid);
    }

}
