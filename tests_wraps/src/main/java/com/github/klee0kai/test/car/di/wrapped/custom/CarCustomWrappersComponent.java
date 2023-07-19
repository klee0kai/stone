package com.github.klee0kai.test.car.di.wrapped.custom;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.*;
import com.github.klee0kai.test.car.model.Car;

@Component(
        wrapperProviders = {
                CarAsyncWrapper.class,
                CarWrapper.class,
        }
)
public interface CarCustomWrappersComponent {
    CarCustomWrapperModule module();

    Car car();

    CarRef<Car> carRef();

    CarLazy<Car> carLazy();

    CarProvide<Car> carProvide();

}
