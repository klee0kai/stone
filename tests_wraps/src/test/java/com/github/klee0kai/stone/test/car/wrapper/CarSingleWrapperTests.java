package com.github.klee0kai.stone.test.car.wrapper;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.types.wrappers.AsyncProvide;
import com.github.klee0kai.stone.types.wrappers.LazyProvide;
import com.github.klee0kai.test.car.di.wrapped.create.CarWrappedCreateComponent;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CarSingleWrapperTests {

    @BeforeEach
    public void init() {
        Bumper.createCount = 0;
        Window.createCount = 0;
        Wheel.createCount = 0;
        Car.createCount = 0;
    }

    @Test
    public void wheelSimpleTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        Wheel wheel1 = DI.wheel();
        Wheel wheel2 = DI.wheel();

        // Then
        assertEquals(1, Wheel.createCount);
        assertNotNull(wheel1.uuid);
        assertEquals(wheel1.uuid, wheel2.uuid);
    }

    @Test
    public void wheelProvideTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        Provider<Wheel> wheel1 = DI.wheelProvide();
        Provider<Wheel> wheel2 = DI.wheelProvide();

        // Then
        assertNotNull(wheel1.get().uuid);
        assertEquals(wheel1.get().uuid, wheel2.get().uuid);
        assertEquals(1, Wheel.createCount);
    }

    @Test
    public void wheelLazyTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        LazyProvide<Wheel> wheel1 = DI.wheelLazy();
        LazyProvide<Wheel> wheel2 = DI.wheelLazy();

        // Then
        assertNotNull(wheel1.get().uuid);
        assertEquals(wheel1.get().uuid, wheel2.get().uuid);
        assertEquals(1, Wheel.createCount);
    }


    @Test
    public void wheelWeakTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        WeakReference<Wheel> wheel1 = DI.wheelWeak();
        WeakReference<Wheel> wheel2 = DI.wheelWeak();

        // Then
        assertEquals(1, Wheel.createCount);
        assertNotNull(wheel1.get().uuid);
        assertEquals(wheel1.get().uuid, wheel2.get().uuid);
    }


    @Test
    public void wheelProvideWeakTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        Provider<WeakReference<Wheel>> wheel1 = DI.whellProviderWeak();
        Provider<WeakReference<Wheel>> wheel2 = DI.whellProviderWeak();

        // Then
        assertNotNull(wheel1.get().get().uuid);
        assertEquals(wheel1.get().get().uuid, wheel2.get().get().uuid);
        assertEquals(1, Wheel.createCount);
    }


    @Test
    public void wheelLazyProvideWeakTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        LazyProvide<Provider<WeakReference<Wheel>>> wheel1 = DI.whellLazyProviderWeak();
        LazyProvide<Provider<WeakReference<Wheel>>> wheel2 = DI.whellLazyProviderWeak();

        // Then
        assertNotNull(wheel1.get().get().get().uuid);
        assertEquals(wheel1.get().get().get().uuid, wheel2.get().get().get().uuid);
        assertEquals(1, Wheel.createCount);
    }


    @Test
    public void wheelProviderTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        Provider<Wheel> wheel1 = DI.whellProvider();
        Provider<Wheel> wheel2 = DI.whellProvider();

        // Then
        assertNotNull(wheel1.get().uuid);
        assertEquals(wheel1.get().uuid, wheel2.get().uuid);
        assertEquals(1, Wheel.createCount);
    }


    @Test
    public void carLazyTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        LazyProvide<Car> car1 = DI.carLazy();
        LazyProvide<Car> car2 = DI.carLazy();

        // Then
        assertEquals(0, Car.createCount);
        assertNotNull(car1.get().uuid);
        assertEquals(car1.get().uuid, car2.get().uuid);
        assertEquals(1, Car.createCount);
    }

    @Test
    public void carAsyncTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        AsyncProvide<Car> car1 = DI.carAsync();
        AsyncProvide<Car> car2 = DI.carAsync();

        // Then
        assertNotNull(car1.get().uuid);
        assertEquals(car1.get().uuid, car2.get().uuid);
        assertEquals(1, Car.createCount);
    }


    @Test
    public void carProvideTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        Provider<Car> car1 = DI.carProvider();
        Provider<Car> car2 = DI.carProvider();

        // Then
        assertEquals(0, Car.createCount);
        assertNotNull(car1.get().uuid);
        assertEquals(car1.get().uuid, car2.get().uuid);
        assertEquals(1, Car.createCount);
    }


    @Test
    public void carWeakTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        WeakReference<Car> car1 = DI.carWeak();
        WeakReference<Car> car2 = DI.carWeak();

        // Then
        assertEquals(1, Car.createCount);
        assertNotNull(car1.get().uuid);
        assertEquals(car1.get().uuid, car2.get().uuid);
    }

    @Test
    public void windowTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        Window window1 = DI.window();
        Window window2 = DI.window();

        // Then
        assertEquals(1, Window.createCount);
        assertNotNull(window1.uuid);
        assertEquals(window1.uuid, window2.uuid);
    }

    @Test
    public void carTest() {
        //Given
        CarWrappedCreateComponent DI = Stone.createComponent(CarWrappedCreateComponent.class);

        //When
        Car car1 = DI.car();
        Car car2 = DI.car();

        // Then
        assertEquals(1, Car.createCount);
        assertNotNull(car1.uuid);
        assertEquals(car1.uuid, car2.uuid);
    }

}
