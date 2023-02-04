package com.dirgub.klee0kai.stone.text_ext.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;
import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import com.github.klee0kai.test_ext.inject.di.base_phone.PhoneExtComponent;
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoodPhoneInjectExtTests {


    @Test
    @Disabled("https://github.com/klee0kai/stone/issues/42")
    public void createdIsReusableTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        GoodPhone goodPhone1 = new GoodPhone();
        DI.inject(goodPhone1, goodPhone1.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //When
        PhoneExtComponent DIPro = Stone.createComponent(PhoneExtComponent.class);
        DIPro.extOf(DI);
        GoodPhone goodPhone2 = new GoodPhone();
        DIPro.inject(goodPhone2, goodPhone2.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //Then
        assertEquals(goodPhone1.ram.uuid, goodPhone2.ram.uuid);
        assertTrue(goodPhone1.ram instanceof DDR3Ram);
    }


    @Test
    public void createGoodPhoneFromProtoTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        PhoneExtComponent DIPro = Stone.createComponent(PhoneExtComponent.class);
        DIPro.extOf(DI);

        GoodPhone goodPhone = new GoodPhone();

        //When
        DI.inject(goodPhone, goodPhone.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //Then
        assertEquals("120GB", goodPhone.dataStorage.size);
        assertEquals("8GB", goodPhone.ram.size);
        assertTrue(goodPhone.ram instanceof DDR3Ram);
    }


    @Test
    public void createGoodPhoneFromProTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        PhoneExtComponent DIPro = Stone.createComponent(PhoneExtComponent.class);
        DIPro.extOf(DI);

        GoodPhone goodPhone = new GoodPhone();

        //When
        DIPro.inject(goodPhone, goodPhone.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //Then
        assertEquals("120GB", goodPhone.dataStorage.size);
        assertEquals("8GB", goodPhone.ram.size);
        assertTrue(goodPhone.ram instanceof DDR3Ram);
    }


    @Test
    public void createGoodPhoneFromProExtTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        PhoneExtComponent DIPro = Stone.createComponent(PhoneExtComponent.class);
        DIPro.extOf(DI);
        GoodPhone goodPhone = new GoodPhone();

        //When
        DIPro.injectExt(goodPhone, goodPhone.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //Then
        assertEquals("120GB", goodPhone.dataStorage.size);
        assertEquals("8GB", goodPhone.ram.size);
        assertTrue(goodPhone.ram instanceof DDR3Ram);
    }


    @Test
    public void cacheInjectedPhoneTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);
        PhoneExtComponent DIPro = Stone.createComponent(PhoneExtComponent.class);
        DIPro.extOf(DI);
        GoodPhone goodPhone1 = new GoodPhone();
        GoodPhone goodPhone2 = new GoodPhone();


        //When
        DI.inject(goodPhone1, goodPhone1.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));
        DIPro.inject(goodPhone2, goodPhone2.lifeCycleOwner, new DataStorageSize("120GB"), new RamSize("8GB"));

        //Then
        assertEquals(goodPhone1.ram.uuid, goodPhone2.ram.uuid);
        assertTrue(goodPhone1.ram instanceof DDR3Ram);
    }

}
