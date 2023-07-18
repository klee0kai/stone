package com.github.klee0kai.stone.test.car.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.CarsInjectQualifiers;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarInjectWithQualifiersTest {

    @Test
    void namedEmptyProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        Car car = carInject.carNamedEmpty;

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
        Car car = carInject.carNamedA;

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
        Car car = carInject.carMyQualifier;

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
        Car car = carInject.carMyQualifierString;

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
        Car car = carInject.carMyQualifierA;

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
        Car car = carInject.carMyQualifierB;

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
        Car car = carInject.carMyQualifierMulti;

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
        Car car = carInject.carMyQualifierMultiA1;

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
        Car car = carInject.carMyQualifierMultiA2;

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
        Car car = carInject.carMyQualifierMultiA2Hard;

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
        assertEquals(14, carInject.allCars.size());
        assertEquals(1, ListUtils.filter(carInject.allCars, (i, it) -> Objects.equals(it.qualifier, "my_qualifier_a")).size());
        assertEquals(1, ListUtils.filter(carInject.allCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi_a2")).size());
        assertEquals(1, ListUtils.filter(carInject.allCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi_a2_hard")).size());
    }


    @Test
    void provideCarsNamedEmptyTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals(1, carInject.carsNamedEmpty.size());
        assertEquals("named_empty", carInject.carsNamedEmpty.get(0).qualifier);
    }

    @Test
    void carsMyQualifierMultiA2HardTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals(1, carInject.carsMyQualifierMultiA2Hard.size());
        assertEquals("qualifier_multi_a2_hard", carInject.carsMyQualifierMultiA2Hard.get(0).qualifier);
    }

}
