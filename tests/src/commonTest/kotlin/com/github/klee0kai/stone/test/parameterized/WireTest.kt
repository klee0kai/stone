package com.github.klee0kai.stone.test.parameterized

import com.github.klee0kai.test.di.wire.WireComponentStoneComponent
import com.github.klee0kai.test.wire.Wire
import com.github.klee0kai.test.wire.types.MiniUsb
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class WireTest {

    @Test
    fun usbUsbProvideTest() {
        //When
        val di = WireComponentStoneComponent()

        //Then
        assertNotNull(di.module().usb_usb())
        assertEquals(
            di.module().usb_usb()!!.uuid,
            di.module().usb_usb()!!.uuid
        )
        assertEquals(
            di.usb_usb()!!.get()!!.uuid,
            di.module().usb_usb()!!.uuid
        )
        assertNotEquals(
            di.module().usb_usb()!!.uuid,
            di.module().usb_hdmi()!!.uuid,
        )
    }

    @Test
    fun miniUsbBindTest() {
        //When
        val di = WireComponentStoneComponent()
        val wire = Wire<MiniUsb?, MiniUsb?>()
        di.miniusb_miniusb(wire)


        //Then
        assertEquals(
            wire.uuid,
            di.miniusb_miniusb(null)!!.uuid,
        )
    }
}
