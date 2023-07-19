package com.github.klee0kai.stone.test.car.wrapper;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.wrapped.custom.CarCustomWrappersComponent;
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarLazy;
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarProvide;
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarRef;
import com.github.klee0kai.test.car.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CurCustomWrapperTests {

    @BeforeEach
    public void init() {
        Car.createCount = 0;
    }

    @Test
    public void carTest() {
        //Given
        CarCustomWrappersComponent DI = Stone.createComponent(CarCustomWrappersComponent.class);

        //When
        Car car1 = DI.car();
        Car car2 = DI.car();

        // Then
        assertEquals(2, Car.createCount);
        assertNotNull(car1.uuid);
        assertNotEquals(car1.uuid, car2.uuid, "factory create");
    }


    @Test
    public void carLayTest() {
        //Given
        CarCustomWrappersComponent DI = Stone.createComponent(CarCustomWrappersComponent.class);

        //When
        CarLazy<Car> car1 = DI.carLazy();
        CarLazy<Car> car2 = DI.carLazy();

        // Then
        assertEquals(0, Car.createCount);
        assertNotNull(car1.getValue().uuid);
        assertEquals(car1.getValue().uuid, car1.getValue().uuid);
        assertNotEquals(car1.getValue().uuid, car2.getValue().uuid);
        assertEquals(2, Car.createCount);
    }

    @Test
    public void carProvideTest() {
        //Given
        CarCustomWrappersComponent DI = Stone.createComponent(CarCustomWrappersComponent.class);

        //When
        CarProvide<Car> car1 = DI.carProvide();
        CarProvide<Car> car2 = DI.carProvide();

        // Then
        assertEquals(0, Car.createCount);
        assertNotNull(car1.getValue().uuid);
        assertNotEquals(car1.getValue().uuid, car1.getValue().uuid);
        assertNotEquals(car1.getValue().uuid, car2.getValue().uuid);
        assertEquals(5, Car.createCount, "getValue has been called 5 times");
    }


    @Test
    public void carRefTest() {
        //Given
        CarCustomWrappersComponent DI = Stone.createComponent(CarCustomWrappersComponent.class);

        //When
        CarRef<Car> car1 = DI.carRef();
        CarRef<Car> car2 = DI.carRef();

        // Then
        assertEquals(2, Car.createCount);
        assertNotNull(car1.getValue().uuid);
        assertNotEquals(car1.getValue().uuid, car2.getValue().uuid);
        assertEquals(car1.getValue().uuid, car1.getValue().uuid);
    }


}
