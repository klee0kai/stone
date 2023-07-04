package com.github.klee0kai.test.car.di.wrapped.create;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.test.car.di.wrapped.create.wrappers.CarLazy;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Provider;
import java.lang.ref.WeakReference;

@Module
public interface CarWrappedCreateModule {

    LazyProvide<Wheel> whell();

    @Provide(cache = Provide.CacheType.Factory)
    Provider<WeakReference<Bumper>> bumper();

    CarLazy<Window> window();

    Car car(Bumper bumper, Wheel wheel, Window window);


}
