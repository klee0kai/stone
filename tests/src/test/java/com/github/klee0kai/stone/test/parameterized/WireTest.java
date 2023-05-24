package com.github.klee0kai.stone.test.parameterized;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.wire.WireComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WireTest {

    @Test
    public void usbUsbTest() {
        //When
        WireComponent di = Stone.createComponent(WireComponent.class);

        //Then
        assertNotNull(di.module().usb_usb());
        assertEquals(di.module().usb_usb().uuid, di.module().usb_usb().uuid);
        assertEquals(di.usb_usb().get().uuid, di.module().usb_usb().uuid);
        assertNotEquals(di.module().usb_usb().uuid, di.module().usb_hdmi().uuid);
    }

}
