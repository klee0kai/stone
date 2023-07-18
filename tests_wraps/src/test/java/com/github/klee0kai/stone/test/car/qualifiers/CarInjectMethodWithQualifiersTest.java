package com.github.klee0kai.stone.test.car.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.CarsInjectQualifiers;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarInjectMethodWithQualifiersTest {

    @Test
    void namedEmptyProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarNamedEmpty;

        //Then
        assertEquals("named_empty", car.qualifier);
        assertEquals("simple", car.bumpers.get(0).qualifier);
        assertEquals(5, car.wheels.size(), "four wheel +1");
    }

    @Test
    void namedAProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarNamedA;

        //Then
        assertEquals("named_a", car.qualifier);
        assertEquals("reinforced", car.bumpers.get(0).qualifier);
        assertEquals(5, car.wheels.size(), "four wheel +1");
    }

    @Test
    void carMyQualifierProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifier;

        //Then
        assertEquals("my_qualifier", car.qualifier);
        assertEquals("simple", car.bumpers.get(0).qualifier);
        assertEquals(5, car.wheels.size(), "four wheel +1");
    }

    @Test
    void carMyQualifierStringProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifierString;

        //Then
        assertEquals("my_qualifier_with_string", car.qualifier);
        assertEquals("simple", car.bumpers.get(0).qualifier);
        assertEquals(5, car.wheels.size(), "four wheel +1");
    }

    @Test
    void carMyQualifierStringAProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifierA;

        //Then
        assertEquals("my_qualifier_a", car.qualifier);
        assertEquals("reinforced", car.bumpers.get(0).qualifier);
        assertEquals(4, car.wheels.size(), "four wheel +1");
    }

    @Test
    void carMyQualifierStringBProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifierB;

        //Then
        assertEquals("my_qualifier_b", car.qualifier);
        assertEquals("simple", car.bumpers.get(0).qualifier);
        assertEquals(5, car.wheels.size(), "four wheel +1");
    }

    @Test
    void carMyQualifierMultiProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifierMulti;

        //Then
        assertEquals("qualifier_multi", car.qualifier);
        assertEquals("simple", car.bumpers.get(0).qualifier);
        assertEquals(4, car.wheels.size());
    }

    @Test
    void carMyQualifierMultiA1ProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifierMultiA1;

        //Then
        assertEquals("qualifier_multi_a1", car.qualifier);
        assertEquals("simple", car.bumpers.get(0).qualifier);
        assertEquals(5, car.wheels.size(), "four wheel +1");
    }


    @Test
    void carMyQualifierMultiA2ProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifierMultiA2;

        //Then
        assertEquals("qualifier_multi_a2", car.qualifier);
        assertEquals("reinforced", car.bumpers.get(0).qualifier);
        assertEquals(4, car.wheels.size());
    }

    @Test
    void carMyQualifierMultiA2HardProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.methodCarMyQualifierMultiA2Hard;

        //Then
        assertEquals("qualifier_multi_a2_hard", car.qualifier);
        assertEquals("simple", car.bumpers.get(0).qualifier);
        assertEquals(4, car.wheels.size());
    }

    @Test
    void allCarsProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals(14, carInject.methodAllCars.size());
        assertEquals(1, ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "my_qualifier_a")).size());
        assertEquals(1, ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi_a2")).size());
        assertEquals(1, ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi_a2_hard")).size());
    }


    @Test
    void provideCarsNamedEmptyTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals(1, carInject.methodCarsNamedEmpty.size());
        assertEquals("named_empty", carInject.methodCarsNamedEmpty.get(0).qualifier);
    }

    @Test
    void carsMyQualifierMultiA2HardTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals(1, carInject.methodCarsMyQualifierMultiA2Hard.size());
        assertEquals("qualifier_multi_a2_hard", carInject.methodCarsMyQualifierMultiA2Hard.get(0).qualifier);
    }

}
