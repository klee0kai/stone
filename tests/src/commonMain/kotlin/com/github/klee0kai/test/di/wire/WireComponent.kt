package com.github.klee0kai.test.di.wire

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.module.BindInstance
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.wire.Wire
import com.github.klee0kai.test.wire.types.Hdmi
import com.github.klee0kai.test.wire.types.MiniUsb
import com.github.klee0kai.test.wire.types.Usb

@Component
abstract class WireComponent {

    abstract fun module(): WireModule?

    abstract fun usb_hdmi(): Wire<Usb?, Hdmi?>?

    abstract fun usb_usb(): WeakRef<Wire<Usb?, Usb?>?>?

    abstract fun simple(): Wire<*, *>?

    @BindInstance
    abstract fun miniusb_miniusb(wire: Wire<MiniUsb?, MiniUsb?>?): Wire<MiniUsb?, MiniUsb?>?

}
