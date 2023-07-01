package com.github.klee0kai.test.car.di_single;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.List;

@Module
public interface CarSingleFactory {

    Car car(Bumper bumper, Wheel wheel);

    LazyProvide<WeakReference<Car>> carRef(Bumper bumper, Wheel wheel);


    Wheel whell();

    Provider<Wheel> whellProvider();


    WeakReference<Ref<Ref<Wheel>>> whellRef();


    Bumper bumper();

    List<Window> windows();

    Window window();

}
