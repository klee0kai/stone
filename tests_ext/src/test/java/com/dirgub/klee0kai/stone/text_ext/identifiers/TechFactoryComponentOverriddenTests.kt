package com.dirgub.klee0kai.stone.text_ext.identifiers

import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent
import com.github.klee0kai.test.di.techfactory.TechFactoryComponentStoneComponent
import com.github.klee0kai.test.tech.components.OperationSystem
import com.github.klee0kai.test.tech.components.Ram
import com.github.klee0kai.test_ext.inject.di.techfactory.TechFactoryExtComponent
import com.github.klee0kai.test_ext.inject.di.techfactory.TechFactoryExtComponentStoneComponent
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class TechFactoryComponentOverriddenTests {

    @Test
    fun nonArgProvideTest() {
        //Given
        val DI: TechFactoryComponent = TechFactoryComponentStoneComponent()
        val DIPro: TechFactoryExtComponent = TechFactoryExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val ram: Ram? = DI.ram()

        //Then
        assertEquals("default", ram!!.size)
        assertTrue(ram is DDR3Ram)
    }

    @Test
    fun singleArgProvideTest() {
        //Given
        val DI: TechFactoryComponent = TechFactoryComponentStoneComponent()
        val DIPro: TechFactoryExtComponent = TechFactoryExtComponentStoneComponent()
        DIPro.extOf(DI)


        //When
        val ram: Ram? = DI.ram(RamSize("4G"))

        //Then
        assertEquals("4G", ram!!.size)
        assertTrue(ram is DDR3Ram)
    }


    @Test
    fun nullGenerateArgProvideTest() {
        //Given
        val DI: TechFactoryComponent = TechFactoryComponentStoneComponent()
        val DIPro: TechFactoryExtComponent = TechFactoryExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val os: OperationSystem? = DI.phoneOs()

        //Then: should pass null missing args
        assertNull(os!!.phoneOsType) // missing args
        assertEquals("default", os.version!!.version) // default from constructor
    }

    @Test
    fun coupleArgsProvideTest() {
        //Given
        val DI: TechFactoryComponent = TechFactoryComponentStoneComponent()
        val DIPro: TechFactoryExtComponent = TechFactoryExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val os: OperationSystem? = DI.phoneOs(PhoneOsType.Ios, PhoneOsVersion("11"))

        //Then
        assertEquals(PhoneOsType.Ios, os!!.phoneOsType)
        assertEquals("11", os.version!!.version)
    }


    @Test
    fun differentCreateTest() {
        //Given
        val DI: TechFactoryComponent = TechFactoryComponentStoneComponent()
        val DIPro: TechFactoryExtComponent = TechFactoryExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val ram8Gb: Ram? = DI.ram(RamSize("8GB"))
        val ram8Gb2: Ram? = DI.ram(RamSize("8GB"))

        //Then: created components are different
        assertEquals("8GB", ram8Gb!!.size)
        assertNotEquals(ram8Gb.uuid, ram8Gb2!!.uuid)
        assertTrue(ram8Gb is DDR3Ram)
        assertTrue(ram8Gb2 is DDR3Ram)
    }


    @Test
    fun enumDifferentTest() {
        //Given
        val DI: TechFactoryComponent = TechFactoryComponentStoneComponent()
        val DIPro: TechFactoryExtComponent = TechFactoryExtComponentStoneComponent()
        DIPro.extOf(DI)

        //When
        val android: OperationSystem? = DI.phoneOs(PhoneOsType.Android)
        val android2: OperationSystem? = DI.phoneOs(PhoneOsType.Android)
        val osNull: OperationSystem? = DI.phoneOs(null)

        //Then: created components are different
        assertNotEquals(android!!.uuid, osNull!!.uuid)
        assertNotEquals(android.uuid, android2!!.uuid)
    }

}
