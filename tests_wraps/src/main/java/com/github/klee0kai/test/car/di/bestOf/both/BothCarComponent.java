package com.github.klee0kai.test.car.di.bestOf.both;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.car.model.Car;

import javax.inject.Named;
import javax.inject.Provider;
import java.lang.ref.WeakReference;

@Component
public interface BothCarComponent {

    BothCarModule myModule();

    @Named("blueCar")
    Provider<Car> blueCar();

    @Named("redCar")
    WeakReference<Car> redCar();

}
