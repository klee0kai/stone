package com.github.klee0kai.stone.test.car.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.qualifiers.CarQComponent;
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

        //Then
        assertEquals("named_empty", carInject.carNamedEmpty.qualifier);
    }

    @Test
    void namedAProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("named_a", carInject.carNamedA.qualifier);
    }

    @Test
    void carMyQualifierProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier", carInject.carMyQualifier.qualifier);
    }

    @Test
    void carMyQualifierStringProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier_with_string", carInject.carMyQualifierString.qualifier);
    }

    @Test
    void carMyQualifierStringAProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier_a", carInject.carMyQualifierA.qualifier);
    }

    @Test
    void carMyQualifierStringBProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("my_qualifier_b", carInject.carMyQualifierB.qualifier);
    }

    @Test
    void carMyQualifierMultiProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi", carInject.carMyQualifierMulti.qualifier);
    }

    @Test
    void carMyQualifierMultiA1ProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi_a1", carInject.carMyQualifierMultiA1.qualifier);
    }


    @Test
    void carMyQualifierMultiA2ProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi_a2", carInject.carMyQualifierMultiA2.qualifier);
    }

    @Test
    void carMyQualifierMultiA2HardProvideTest() {
        //Given
        CarQComponent DI = Stone.createComponent(CarQComponent.class);
        CarsInjectQualifiers carInject = new CarsInjectQualifiers();

        //When
        DI.inject(carInject);

        //Then
        assertEquals("qualifier_multi_a2_hard", carInject.carMyQualifierMultiA2Hard.qualifier);
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
