package com.github.klee0kai.test.car.di.bestOf.blue;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.car.di.bestOf.both.BothCarModule;
import com.github.klee0kai.test.car.model.Car;

import javax.inject.Provider;

@Component
public interface BlueCarComponent {

    BothCarModule myModule();

    Provider<Car> blueCar();

}
