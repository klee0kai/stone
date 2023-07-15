package com.github.klee0kai.stone.test.car.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent;
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

        //Then
        assertEquals("named_empty", carInject.methodCarNamedEmpty.qualifier);
    }

    @Test
    void namedAProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("named_a", carInject.methodCarNamedA.qualifier);
    }

    @Test
    void carMyQualifierProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier", carInject.methodCarMyQualifier.qualifier);
    }

    @Test
    void carMyQualifierStringProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier_with_string", carInject.methodCarMyQualifierString.qualifier);
    }

    @Test
    void carMyQualifierStringAProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier_a", carInject.methodCarMyQualifierA.qualifier);
    }

    @Test
    void carMyQualifierStringBProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier_b", carInject.methodCarMyQualifierB.qualifier);
    }

    @Test
    void carMyQualifierMultiProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi", carInject.methodCarMyQualifierMulti.qualifier);
    }

    @Test
    void carMyQualifierMultiA1ProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi_a1", carInject.methodCarMyQualifierMultiA1.qualifier);
    }


    @Test
    void carMyQualifierMultiA2ProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi_a2", carInject.methodCarMyQualifierMultiA2.qualifier);
    }

    @Test
    void carMyQualifierMultiA2HardProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi_a2_hard", carInject.methodCarMyQualifierMultiA2Hard.qualifier);
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
