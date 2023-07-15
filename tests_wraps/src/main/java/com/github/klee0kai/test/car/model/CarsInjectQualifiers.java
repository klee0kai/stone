package com.github.klee0kai.test.car.model;

import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifier;

import javax.inject.Inject;

public class CarsInjectQualifiers {


    @MyQualifier
    public Car carMyQualifier;

    @Inject
    public void init(Car car, Car carMyQualifier) {

    }

}
