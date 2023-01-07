package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.test.di.base_phone.qualifiers.DataStorageSize;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.tech.phone.GoodPhone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InjectQualifiersTests {


    @Test
    public void createGoodPhoneTest() {
        //When
        GoodPhone goodPhone = new GoodPhone(new DataStorageSize("120GB"), new RamSize("8GB"));
        goodPhone.buy();

        //Then
        assertEquals("120GB", goodPhone.dataStorage.size);
        assertEquals("8GB", goodPhone.ram.size);
    }

}
