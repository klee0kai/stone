package com.github.klee0kai.test.car.di.bestOf.red;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.car.di.bestOf.both.BothCarModule;
import com.github.klee0kai.test.car.model.Car;

import javax.inject.Named;
import java.lang.ref.WeakReference;

@Component
public interface RedCarComponent {

    BothCarModule myModule();

    @Named("redCar")
    WeakReference<Car> redCar();

}
