package com.github.klee0kai.test.car.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.test.car.Car;
import com.github.klee0kai.test.car.Whell;
import com.github.klee0kai.test.car.Window;
import com.github.klee0kai.test.car.di.wrappers.CarWrappers;
import com.github.klee0kai.test.car.di.wrappers.CarWrappes2;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.List;

@Component(
        wrapperProviders = {CarWrappers.class, CarWrappes2.class}
)
public interface CarBuilding {

    CarFactory factory();


    Whell whell();

    Provider<Whell> whellProvide();

    LazyProvide<Whell> whellLazy();

    WeakReference<Whell> whellWeak();

    Provider<WeakReference<Whell>> whellProviderWeak();

    LazyProvide<Provider<WeakReference<Whell>>> whellLazyProviderWeak();

    Provider<Whell> whellProvider();

    List<Provider<Whell>> allWhellProviders();

    Car car();

    LazyProvide<Car> carLazy();

    Provider<Car> carProvider();

    WeakReference<Car> carWeak();

    List<Window> windows();

    Window window();

}
