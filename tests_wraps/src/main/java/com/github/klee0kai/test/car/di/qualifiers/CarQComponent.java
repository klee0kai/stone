package com.github.klee0kai.test.car.di.qualifiers;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifier;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierMulti;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierWithString;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.CarsInjectQualifiers;

import javax.inject.Named;
import java.util.List;

@Component
public interface CarQComponent {

    CarQPModule module1();


    CarQCModule module2();


    @Named()
    Car carNamedEmpty();

    @Named("a")
    Car carNameA();

    @MyQualifier
    Car carMyQualifier();

    @MyQualifierWithString
    Car carMyQualifierString();

    @MyQualifierWithString(id = "a")
    Car carMyQualifierStringA();

    @MyQualifierWithString(id = "b")
    Car carMyQualifierStringB();

    @MyQualifierMulti()
    Car carMyQualifierMulti();

    @MyQualifierMulti(indx = 1, id = "a")
    Car carMyQualifierMultiA1();


    @MyQualifierMulti(id = "a", indx = 2)
    Car carMyQualifierMultiA2();

    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, indx = 2, id = "a")
    Car carMyQualifierMultiA2Hard();

    List<Car> allCars();

    @Named
    List<Car> provideCarsNamedEmpty();

    @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, indx = 2, id = "a")
    List<Car> carsMyQualifierMultiA2Hard();

    void inject(CarsInjectQualifiers carInject);
}
