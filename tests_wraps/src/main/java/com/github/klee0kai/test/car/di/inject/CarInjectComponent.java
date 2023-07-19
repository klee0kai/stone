package com.github.klee0kai.test.car.di.inject;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarProviderWrapper;
import com.github.klee0kai.test.car.model.CarInject;
import com.github.klee0kai.test.car.model.CarInjectLists;
import com.github.klee0kai.test.car.model.CarInjectProvider;

@Component(
        wrapperProviders = {CarProviderWrapper.class}
)
public abstract class CarInjectComponent {

    protected abstract CarInjectModule module();

    public abstract void inject(CarInject carInject);

    public abstract void inject(CarInjectLists carInject);

    public abstract void inject(CarInjectProvider carInject);

    @ProtectInjected
    public abstract void protect(CarInject carInject);

    @ProtectInjected
    public abstract void protect(CarInjectLists carInject);

    @ProtectInjected
    public abstract void protect(CarInjectProvider carInject);

}
