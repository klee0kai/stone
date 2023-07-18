package com.github.klee0kai.test.car.model;

import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifier;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierMulti;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierWithString;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class CarsInjectQualifiers {

    @Inject
    @Named
    public Car carNamedEmpty;

    @Inject
    @Named("a")
    public Car carNamedA;

    @Inject
    @MyQualifier
    public Car carMyQualifier;

    @Inject
    @MyQualifierWithString
    public Car carMyQualifierString;

    @Inject
    @MyQualifierWithString(id = "a")
    public Car carMyQualifierA;

    @Inject
    @MyQualifierWithString(id = "b")
    public Car carMyQualifierB;

    @Inject
    @MyQualifierMulti()
    public Car carMyQualifierMulti;

    @Inject
    @MyQualifierMulti(indx = 1, id = "a")
    public Car carMyQualifierMultiA1;

    @Inject
    @MyQualifierMulti(indx = 2, id = "a")
    public Car carMyQualifierMultiA2;

    @Inject
    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2)
    public Car carMyQualifierMultiA2Hard;

    @Inject
    public List<Car> allCars;

    @Inject
    @Named
    public List<Car> carsNamedEmpty;

    @Inject
    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2)
    public List<Car> carsMyQualifierMultiA2Hard;


    public Car methodCarNamedEmpty;
    public Car methodCarNamedA;
    public Car methodCarMyQualifier;
    public Car methodCarMyQualifierString;
    public Car methodCarMyQualifierA;
    public Car methodCarMyQualifierB;
    public Car methodCarMyQualifierMulti;
    public Car methodCarMyQualifierMultiA1;
    public Car methodCarMyQualifierMultiA2;
    public Car methodCarMyQualifierMultiA2Hard;
    public List<Car> methodAllCars;
    public List<Car> methodCarsNamedEmpty;
    public List<Car> methodCarsMyQualifierMultiA2Hard;

    @Inject
    public void init(
            @Named Car carNamedEmpty,
            @Named("a") Car carNamedA,
            @MyQualifier Car carMyQualifier,
            @MyQualifierWithString Car carMyQualifierString,
            @MyQualifierWithString(id = "a") Car carMyQualifierA,
            @MyQualifierWithString(id = "b") Car carMyQualifierB,
            @MyQualifierMulti() Car carMyQualifierMulti,
            @MyQualifierMulti(indx = 1, id = "a") Car carMyQualifierMultiA1,
            @MyQualifierMulti(indx = 2, id = "a") Car carMyQualifierMultiA2,
            @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2)
            Car carMyQualifierMultiA2Hard,
            List<Car> allCars,
            @Named List<Car> carsNamedEmpty,
            @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, id = "a", indx = 2)
            List<Car> carsMyQualifierMultiA2Hard
    ) {
        methodCarNamedEmpty = carNamedEmpty;
        methodCarNamedA = carNamedA;
        methodCarMyQualifier = carMyQualifier;
        methodCarMyQualifierString = carMyQualifierString;
        methodCarMyQualifierA = carMyQualifierA;
        methodCarMyQualifierB = carMyQualifierB;
        methodCarMyQualifierMulti = carMyQualifierMulti;
        methodCarMyQualifierMultiA1 = carMyQualifierMultiA1;
        methodCarMyQualifierMultiA2 = carMyQualifierMultiA2;
        methodCarMyQualifierMultiA2Hard = carMyQualifierMultiA2Hard;
        methodAllCars = allCars;
        methodCarsNamedEmpty = carsNamedEmpty;
        methodCarsMyQualifierMultiA2Hard = carsMyQualifierMultiA2Hard;
    }

}
