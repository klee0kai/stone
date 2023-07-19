package com.github.klee0kai.test.car.di.wrapped.create;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.wrappers.AsyncProvide;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Provider;
import java.lang.ref.WeakReference;

@Component
public interface CarWrappedCreateComponent {

    CarWrappedCreateModule factory();

    Wheel wheel();

    Provider<Wheel> wheelProvide();

    LazyProvide<Wheel> wheelLazy();

    WeakReference<Wheel> wheelWeak();

    Provider<WeakReference<Wheel>> whellProviderWeak();

    LazyProvide<Provider<WeakReference<Wheel>>> whellLazyProviderWeak();

    Provider<Wheel> whellProvider();

    LazyProvide<Car> carLazy();

    Provider<Car> carProvider();

    WeakReference<Car> carWeak();

    Window window();

    Car car();

    AsyncProvide<Car> carAsync();

}
