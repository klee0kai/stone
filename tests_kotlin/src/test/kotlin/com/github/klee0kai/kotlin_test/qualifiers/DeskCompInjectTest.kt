package com.github.klee0kai.kotlin_test.qualifiers

import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.KConnectType
import com.github.klee0kai.test_kotlin.di.base_comp.qualifiers.MonitorSize
import com.github.klee0kai.test_kotlin.tech.comp.DesktopComp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DeskCompInjectTest {

    @Test
    fun buyDefDeskCompTest() {
        //When
        val desktopComp = DesktopComp()
        desktopComp.buy()

        //Then
        assertEquals("17", desktopComp.monitor?.size?.size)
        assertEquals(KConnectType.Din6Connector, desktopComp.keyboard?.KConnectType)
    }


    @Test
    fun buyLargeMonitorDeskCompTest() {
        //When
        val desktopComp = DesktopComp(
            monitorSize = MonitorSize("27"),
            kConnectType = KConnectType.UsbRemote,
        )
        desktopComp.buy()

        //Then
        assertEquals("27", desktopComp.monitor?.size?.size)
        assertEquals(KConnectType.UsbRemote, desktopComp.keyboard?.KConnectType)
    }

}