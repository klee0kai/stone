package com.github.klee0kai.test.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.test.car.Car;
import com.github.klee0kai.test.car.Whell;

import javax.inject.Provider;
import java.lang.ref.WeakReference;

@Component
public interface CarBuilding {

    CarFactory factory();


    Whell whell();

    Provider<Whell> whellProvide();

    LazyProvide<Whell> whellLazy();

    WeakReference<Whell> whellWeak();

    Provider<Whell> whellProvider();

    Car car();

    LazyProvide<Car> carLazy();

    Provider<Car> carProvider();

    WeakReference<Car> carWeak();

}
