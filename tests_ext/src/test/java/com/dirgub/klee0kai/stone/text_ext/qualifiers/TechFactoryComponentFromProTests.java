package com.dirgub.klee0kai.stone.text_ext.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsVersion;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;
import com.github.klee0kai.test_ext.inject.di.techfactory.TechFactoryExtComponent;
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TechFactoryComponentFromProTests {

    @Test
    public void nonArgProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);
        TechFactoryExtComponent DIPro = Stone.createComponent(TechFactoryExtComponent.class);
        DIPro.extOf(DI);

        //When
        Ram ram = DIPro.ram();

        //Then
        assertEquals("default", ram.size);
        assertTrue(ram instanceof DDR3Ram);
    }

    @Test
    public void singleArgProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);
        TechFactoryExtComponent DIPro = Stone.createComponent(TechFactoryExtComponent.class);
        DIPro.extOf(DI);


        //When
        Ram ram = DIPro.ram(new RamSize("4G"));

        //Then
        assertEquals("4G", ram.size);
        assertTrue(ram instanceof DDR3Ram);
    }


    @Test
    public void nullGenerateArgProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);
        TechFactoryExtComponent DIPro = Stone.createComponent(TechFactoryExtComponent.class);
        DIPro.extOf(DI);

        //When
        OperationSystem os = DIPro.phoneOs();

        //Then: should pass null missing args
        assertNull(os.phoneOsType); // missing args
        assertEquals("default", os.version.version); // default from constructor
    }

    @Test
    public void coupleArgsProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);
        TechFactoryExtComponent DIPro = Stone.createComponent(TechFactoryExtComponent.class);
        DIPro.extOf(DI);

        //When
        OperationSystem os = DIPro.phoneOs(PhoneOsType.Ios, new PhoneOsVersion("11"));

        //Then
        assertEquals(PhoneOsType.Ios, os.phoneOsType);
        assertEquals("11", os.version.version);
    }


    @Test
    public void differentCreateTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);
        TechFactoryExtComponent DIPro = Stone.createComponent(TechFactoryExtComponent.class);
        DIPro.extOf(DI);

        //When
        Ram ram8Gb = DIPro.ram(new RamSize("8GB"));
        Ram ram8Gb2 = DIPro.ram(new RamSize("8GB"));

        //Then: created components are different
        assertEquals("8GB", ram8Gb.size);
        assertNotEquals(ram8Gb.uuid, ram8Gb2.uuid);
        assertTrue(ram8Gb instanceof DDR3Ram);
        assertTrue(ram8Gb2 instanceof DDR3Ram);
    }


    @Test
    public void enumDifferentTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);
        TechFactoryExtComponent DIPro = Stone.createComponent(TechFactoryExtComponent.class);
        DIPro.extOf(DI);

        //When
        OperationSystem android = DIPro.phoneOs(PhoneOsType.Android);
        OperationSystem android2 = DIPro.phoneOs(PhoneOsType.Android);
        OperationSystem osNull = DIPro.phoneOs(null);

        //Then: created components are different
        assertNotEquals(android.uuid, osNull.uuid);
        assertNotEquals(android.uuid, android2.uuid);
    }


}
