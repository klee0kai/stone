package com.github.klee0kai.stone.test.car.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.CarsInjectQualifiers;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarInjectAllMethodsWithQualifiersTest {

    @Test
    void namedEmptyProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "named_empty"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "named_a"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "my_qualifier"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "my_qualifier_with_string"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "my_qualifier_a"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "my_qualifier_b"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi_a1"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi_a2"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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
        List<Car> filtered = ListUtils.filter(carInject.methodAllCars, (i, it) -> Objects.equals(it.qualifier, "qualifier_multi_a2_hard"));
        Car car = filtered.get(0);

        //Then
        assertEquals(1, filtered.size());
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

}
