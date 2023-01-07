package com.github.klee0kai.stone.test.qualifiers;

import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InjectGoodPhoneTests {


    @Test
    public void createGoodPhoneTest() {
        //When
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("120GB"), new RamSize("8GB"));
        goodPhone.buy();

        //Then
        assertEquals("120GB", goodPhone.dataStorage.size);
        assertEquals("8GB", goodPhone.ram.size);
    }

    @Test
    public void cacheInjectedPhoneTest() {
        GoodPhone goodPhone1 = new GoodPhone(new DataStorageSize("120GB"), new RamSize("8GB"));
        GoodPhone goodPhone2 = new GoodPhone(new DataStorageSize("120GB"), new RamSize("8GB"));

        //When
        goodPhone1.buy();
        goodPhone2.buy();

        //Then
        assertEquals(goodPhone1.ram.uuid, goodPhone2.ram.uuid);
    }

}
