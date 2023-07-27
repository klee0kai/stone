package com.github.klee0kai.test.car.di.wrapped.create;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import java.lang.ref.WeakReference;

@Module
public interface CarWrappedCreateModule {


    @Provide(cache = Provide.CacheType.Soft)
    Wheel whell();

    WeakReference<Bumper> bumper();


    @Provide(cache = Provide.CacheType.Soft)
    Window window();


    @Provide(cache = Provide.CacheType.Soft)
    Car car(Bumper bumper, Wheel wheel, Window window);


}
