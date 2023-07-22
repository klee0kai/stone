package com.github.klee0kai.test.car.di.cachecontrol.gc;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperRedScope;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcBumperScope;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWheelScope;
import com.github.klee0kai.test.car.di.cachecontrol.gc.scopes.GcWindowScope;

@Component(
        identifiers = {String.class, Integer.class}
)
public abstract class CarGcComponent {

    public abstract BumperGcModule bumpersModule();

    public abstract WheelGcModule wheelsModule();

    public abstract WheelMappedGcModule wheelMappedModule();

    public abstract WheelMultiMappedGcModule wheelMultiMappedModule();

    public abstract WindowGcModule windowsModule();

    public abstract WindowMappedGcModule windowsMappedModule();

    public abstract WindowMultiMappedGcModule windowsMultiMappedModule();

    @GcAllScope
    public abstract void gcAll();

    @GcWeakScope
    public abstract void gcWeak();

    @GcSoftScope
    public abstract void gcSoft();

    @GcStrongScope
    public abstract void gcStrong();

    @GcWheelScope
    public abstract void gcWheels();

    @GcBumperScope
    public abstract void gcBumpers();

    @GcBumperRedScope
    public abstract void gcRedBumpers();

    @GcBumperRedScope
    @GcBumperScope
    public abstract void gcRedBumpers2();

    @GcWindowScope
    public abstract void gcWindows();

    @GcWindowScope
    @GcWeakScope
    public abstract void gcWeakWindows();

    @GcWindowScope
    @GcSoftScope
    public abstract void gcSoftWindows();

    @GcWindowScope
    @GcBumperScope
    public abstract void gcNothing();

    public void gcWindowsAndWheels() {
        gcWindows();
        gcWheels();
    }


}
