package com.github.klee0kai.test.car.di_single;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.test.car.di_single.wrappers.CarWrappes2;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;
import com.github.klee0kai.test.car.di_single.wrappers.CarWrappers;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.List;

//@Component(
//        wrapperProviders = {CarWrappers.class, CarWrappes2.class}
//)
public interface CarSingleBuilding {

    CarSingleFactory factory();


    Wheel whell();

    Provider<Wheel> whellProvide();

    LazyProvide<Wheel> whellLazy();

    WeakReference<Wheel> whellWeak();

    Provider<WeakReference<Wheel>> whellProviderWeak();

    LazyProvide<Provider<WeakReference<Wheel>>> whellLazyProviderWeak();

    Provider<Wheel> whellProvider();

    List<Provider<Wheel>> allWhellProviders();

    Car car();

    LazyProvide<Car> carLazy();

    Provider<Car> carProvider();

    WeakReference<Car> carWeak();

    List<Window> windows();

    Window window();

}
