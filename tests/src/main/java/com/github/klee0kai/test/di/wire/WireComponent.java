package com.github.klee0kai.test.di.wire;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.wire.Wire;
import com.github.klee0kai.test.wire.types.Hdmi;
import com.github.klee0kai.test.wire.types.Usb;

import java.lang.ref.WeakReference;

@Component
public abstract class WireComponent {

    public abstract WireModule module();

    public abstract Wire<Usb, Hdmi> usb_hdmi();

    public abstract WeakReference<Wire<Usb, Usb>> usb_usb();

    public abstract Wire simple();

}
