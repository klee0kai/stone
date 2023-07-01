package com.github.klee0kai.test.car.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.test.car.Bumper;
import com.github.klee0kai.test.car.Car;
import com.github.klee0kai.test.car.Whell;
import com.github.klee0kai.test.car.Window;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.List;

@Module
public interface CarFactory {

    Car car(Bumper bumper, Whell whell);

    LazyProvide<WeakReference<Car>> carRef(Bumper bumper, Whell whell);


    Whell whell();

    Provider<Whell> whellProvider();


    WeakReference<Ref<Ref<Whell>>> whellRef();


    Bumper bumper();

    List<Window> windows();

}
