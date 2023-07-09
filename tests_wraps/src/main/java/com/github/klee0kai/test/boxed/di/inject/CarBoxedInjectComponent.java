package com.github.klee0kai.test.boxed.di.inject;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.test.boxed.model.CarBoxedInject;
import com.github.klee0kai.test.boxed.model.CarBoxedInjectLists;
import com.github.klee0kai.test.boxed.model.CarBoxedInjectProvider;

@Component
public abstract class CarBoxedInjectComponent {

    protected abstract CarBoxedInjectModule module();

    public abstract void inject(CarBoxedInject carInject);

    public abstract void inject(CarBoxedInjectLists carInject);

    public abstract void inject(CarBoxedInjectProvider carInject);

    @ProtectInjected
    public abstract void protect(CarBoxedInject carInject);

    @ProtectInjected
    public abstract void protect(CarBoxedInjectLists carInject);

    @ProtectInjected
    public abstract void protect(CarBoxedInjectProvider carInject);

}
