package com.github.klee0kai.test.car.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.car.Bumper;
import com.github.klee0kai.test.car.Car;
import com.github.klee0kai.test.car.Whell;

@Module
public interface CarFactory {

    Car car(Bumper bumper, Whell whell);

    Whell whell();


    Bumper bumper();

}
