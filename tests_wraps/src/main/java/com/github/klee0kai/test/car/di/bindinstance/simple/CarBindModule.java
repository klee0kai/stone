package com.github.klee0kai.test.car.di.bindinstance.simple;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import java.lang.ref.WeakReference;
import java.util.List;

@Module
public interface CarBindModule {

    @BindInstance
    Wheel wheel();

    @BindInstance
    WeakReference<Bumper> bumper();

    @BindInstance
    WeakReference<List<Window>> windows();

}
