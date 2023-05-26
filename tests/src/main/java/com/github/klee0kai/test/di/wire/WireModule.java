package com.github.klee0kai.test.di.wire;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.wire.Wire;
import com.github.klee0kai.test.wire.types.Hdmi;
import com.github.klee0kai.test.wire.types.MiniUsb;
import com.github.klee0kai.test.wire.types.Usb;

@Module
public abstract class WireModule {

    public abstract Wire<Usb, MiniUsb> usb_miniusb();

    public abstract Wire<Usb, Usb> usb_usb();

    public Wire<Usb, Hdmi> usb_hdmi() {
        return new Wire<>();
    }


    public Wire simple() {
        return new Wire<Usb, Usb>();
    }


}
