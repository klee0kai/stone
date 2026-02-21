package com.github.klee0kai.test.di.wire

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.wire.Wire
import com.github.klee0kai.test.wire.types.Hdmi
import com.github.klee0kai.test.wire.types.MiniUsb
import com.github.klee0kai.test.wire.types.Usb

@Module
abstract class WireModule {

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun usb_miniusb(): Wire<Usb?, MiniUsb?>?

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun usb_usb(): Wire<Usb?, Usb?>?

    @Provide(cache = Provide.CacheType.Soft)
    open fun usb_hdmi(): Wire<Usb?, Hdmi?>? {
        return Wire<Usb?, Hdmi?>()
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun simple(): Wire<*, *>? {
        return Wire<Usb?, Usb?>()
    }

}
