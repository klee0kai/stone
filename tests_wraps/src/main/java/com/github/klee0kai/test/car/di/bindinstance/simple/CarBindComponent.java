package com.github.klee0kai.test.car.di.bindinstance.simple;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

@Component
public interface CarBindComponent {

    CarBindModule module();

    @BindInstance
    void bindWheel(Wheel wheel);

    @BindInstance
    void bindWheelRef(Reference<Wheel> wheel);

    @BindInstance
    void bindBumper(Ref<Bumper> bumper);

    @BindInstance
    void bindWindow(Window window);

    @BindInstance
    void bindWindows(Collection<Window> window);

    @BindInstance
    void bindWindowRefs(Collection<WeakReference<Window>> window);

    Wheel provideWheel();

    Reference<Wheel> provideWheelRef();

    List<Reference<Wheel>> provideWheels();

    Ref<Bumper> provideBumper();

    List<Bumper> provideBumpers();

    Window provideWindow();

    Collection<Window> provideWindows();

}
