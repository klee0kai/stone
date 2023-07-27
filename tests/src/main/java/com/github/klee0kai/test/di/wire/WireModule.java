package com.github.klee0kai.test.di.wire;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.wire.Wire;
import com.github.klee0kai.test.wire.types.Hdmi;
import com.github.klee0kai.test.wire.types.MiniUsb;
import com.github.klee0kai.test.wire.types.Usb;

@Module
public abstract class WireModule {

    @Provide(cache = Provide.CacheType.Soft)
    public abstract Wire<Usb, MiniUsb> usb_miniusb();

    @Provide(cache = Provide.CacheType.Soft)
    public abstract Wire<Usb, Usb> usb_usb();

    @Provide(cache = Provide.CacheType.Soft)
    public Wire<Usb, Hdmi> usb_hdmi() {
        return new Wire<>();
    }

    @Provide(cache = Provide.CacheType.Soft)
    public Wire simple() {
        return new Wire<Usb, Usb>();
    }


}
