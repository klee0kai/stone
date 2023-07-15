package com.github.klee0kai.test.car.di.qualifiers;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import java.lang.ref.WeakReference;
import java.util.List;

@Module
public interface CarQPModule {

    @Provide(cache = Provide.CacheType.Factory)
    Wheel wheel();

    @Provide(cache = Provide.CacheType.Factory)
    WeakReference<Bumper> bumper();

    @Provide(cache = Provide.CacheType.Factory)
    WeakReference<List<Window>> windows();

}
