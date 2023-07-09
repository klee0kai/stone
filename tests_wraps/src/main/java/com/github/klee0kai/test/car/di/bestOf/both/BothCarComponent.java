package com.github.klee0kai.test.car.di.bestOf.both;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.car.model.Car;

import javax.inject.Provider;
import java.lang.ref.WeakReference;

@Component
public interface BothCarComponent {

    BothCarModule myModule();

    Provider<Car> blueCar();

    WeakReference<Car> redCar();

}
