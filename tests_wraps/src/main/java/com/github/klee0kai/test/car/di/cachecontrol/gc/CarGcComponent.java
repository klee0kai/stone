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

    @RunGc
    @GcAllScope
    public abstract void gcAll();

    @RunGc
    @GcWeakScope
    public abstract void gcWeak();

    @RunGc
    @GcSoftScope
    public abstract void gcSoft();

    @RunGc
    @GcStrongScope
    public abstract void gcStrong();

    @RunGc
    @GcWheelScope
    public abstract void gcWheels();

    @RunGc
    @GcBumperScope
    public abstract void gcBumpers();

    @RunGc
    @GcBumperRedScope
    public abstract void gcRedBumpers();

    @RunGc
    @GcBumperRedScope
    @GcBumperScope
    public abstract void gcRedBumpers2();

    @RunGc
    @GcWindowScope
    public abstract void gcWindows();

    @RunGc
    @GcWindowScope
    @GcWeakScope
    public abstract void gcWeakWindows();

    @RunGc
    @GcWindowScope
    @GcSoftScope
    public abstract void gcSoftWindows();

    @RunGc
    @GcWindowScope
    @GcBumperScope
    public abstract void gcNothing();

    public void gcWindowsAndWheels() {
        gcWindows();
        gcWheels();
    }


}
