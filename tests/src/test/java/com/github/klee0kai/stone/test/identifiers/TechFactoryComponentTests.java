package com.github.klee0kai.stone.test.identifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TechFactoryComponentTests {

    @Test
    public void nonArgProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        Ram ram = DI.ram();

        //Then
        assertEquals("default", ram.size);
    }

    @Test
    public void singleArgProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        Ram ram = DI.ram(new RamSize("4G"));

        //Then
        assertEquals("4G", ram.size);
    }


    @Test
    public void nullGenerateArgProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        OperationSystem os = DI.phoneOs();

        //Then: should pass null missing args
        assertNull(os.phoneOsType); // missing args
        assertEquals("default", os.version.version); // default from constructor
    }

    @Test
    public void coupleArgsProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        OperationSystem os = DI.phoneOs(PhoneOsType.Ios, new PhoneOsVersion("11"));

        //Then
        assertEquals(PhoneOsType.Ios, os.phoneOsType);
        assertEquals("11", os.version.version);
    }


    @Test
    public void differentCreateTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        Ram ram8Gb = DI.ram(new RamSize("8GB"));
        Ram ram8Gb2 = DI.ram(new RamSize("8GB"));

        //Then: created components are different
        assertEquals("8GB", ram8Gb.size);
        assertNotEquals(ram8Gb.uuid, ram8Gb2.uuid);
    }


    @Test
    public void enumDifferentTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        OperationSystem android = DI.phoneOs(PhoneOsType.Android);
        OperationSystem android2 = DI.phoneOs(PhoneOsType.Android);
        OperationSystem osNull = DI.phoneOs(null);

        //Then: created components are different
        assertNotEquals(android.uuid, osNull.uuid);
        assertNotEquals(android.uuid, android2.uuid);
    }


}
