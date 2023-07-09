package com.github.klee0kai.stone.test.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.inject.CarInjectComponent;
import com.github.klee0kai.test.car.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CarInjectMixedTests {

    @BeforeEach
    public void init() {
        Bumper.createCount = 0;
        Window.createCount = 0;
        Wheel.createCount = 0;
        Car.createCount = 0;
    }

    @Test
    void carInjectReusableTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInject carInject1 = new CarInject();
        CarInject carInject2 = new CarInject();
        DI.inject(carInject1);
        DI.inject(carInject2);


        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused");
        assertEquals(carInject1.bumperFromMethod.uuid, carInject2.bumper.uuid, "should cache");
        assertEquals(carInject1.wheelFromMethod.uuid, carInject2.wheel.uuid, "should cache ");
        assertNotEquals(carInject1.windowFromMethod.uuid, carInject2.window.uuid, "provide via Provider. No caching");
    }


    @Test
    void carInjectListReusableTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInjectLists carInject1 = new CarInjectLists();
        CarInjectLists carInject2 = new CarInjectLists();
        DI.inject(carInject1);
        DI.inject(carInject2);

        // Then
        assertEquals(2, carInject1.bumpers.size());
        assertEquals(5, carInject1.wheels.size());
        assertEquals(4, carInject1.windows.size());
        for (int i = 0; i < 2; i++) assertEquals(carInject1.bumpersMethodFrom.get(i).uuid, carInject2.bumpers.get(i).uuid);
        for (int i = 0; i < 2; i++) assertEquals(carInject1.wheelsMethodFrom.get(i).uuid, carInject2.wheels.get(i).uuid);
        Set<String> windowUids = new HashSet<>();
        for (int i = 0; i < 4; i++) windowUids.add(carInject1.windowsMethodFrom.get(i).uuid);
        for (int i = 0; i < 4; i++) windowUids.add(carInject2.windows.get(i).uuid);
        assertEquals(8, windowUids.size());
    }


    @Test
    void carInjectProvideReusableTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInjectProvider carInject1 = new CarInjectProvider();
        CarInjectProvider carInject2 = new CarInjectProvider();
        DI.inject(carInject1);
        DI.inject(carInject2);


        // Then
        assertEquals(0, Bumper.createCount, "Provider non used");
        assertEquals(carInject1.bumperFromMethod.get().uuid, carInject2.bumper.get().uuid, "should cache");
        assertEquals(carInject1.wheelFromMethod.get().uuid, carInject2.wheel.get().uuid, "should cache ");
        assertNotEquals(carInject1.windowFromMethod.get().getValue().uuid, carInject2.window.get().getValue().uuid, "provide via Provider. No caching");
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused");
    }

}
