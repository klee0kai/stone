package com.github.klee0kai.stone.test.lists.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.inject.CarInjectComponent;
import com.github.klee0kai.test.car.model.*;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class CarInjectTests {

    public static long startTime = 0;

    @BeforeAll
    public static void measurePerfStart() {
        startTime = System.currentTimeMillis();
    }

    @AfterAll
    public static void measurePerfEnd() {
        long endTime = System.currentTimeMillis();
        long spendTime = endTime - startTime;
        System.out.println("test time: " + spendTime + " ms");
        assertTrue(spendTime < 500, "Spend time " + spendTime + " ms");
    }

    @BeforeEach
    public void init() {
        Bumper.createCount = 0;
        Window.createCount = 0;
        Wheel.createCount = 0;
        Car.createCount = 0;
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    void carInjectTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInject carInject = new CarInject();
        DI.inject(carInject);

        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list");
        assertNotNull(carInject.bumper.uuid);
        assertNotNull(carInject.wheel.uuid);
        assertNotNull(carInject.window.uuid);
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
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
        assertEquals(carInject1.bumper.uuid, carInject2.bumper.uuid, "should cache");
        assertEquals(carInject1.wheel.uuid, carInject2.wheel.uuid, "should cache ");
        assertNotEquals(carInject1.window.uuid, carInject2.window.uuid, "provide via Provider. No caching");
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    @Disabled("TODO")
    void carProtectInjectedTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInject carInject = new CarInject();
        DI.inject(carInject);
        String bumperUid = carInject.bumper.uuid;
        String wheelUid = carInject.wheel.uuid;
        String windowUid = carInject.window.uuid;
        DI.protect(carInject);
        carInject = null;
        System.gc();
        carInject = new CarInject();
        DI.inject(carInject);


        // Then
        assertEquals(bumperUid, carInject.bumper.uuid);
        assertEquals(wheelUid, carInject.wheel.uuid);
        assertNotEquals(windowUid, carInject.window.uuid);
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    void carInjectListTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInjectLists carInject = new CarInjectLists();
        DI.inject(carInject);

        // Then
        assertEquals(2, Bumper.createCount);
        assertEquals(5, Wheel.createCount);
        assertEquals(8, Window.createCount, "Window created for fields and method");
        assertEquals(2, carInject.bumpers.size());
        assertEquals(5, carInject.wheels.size());
        assertEquals(4, carInject.windows.size());
        for (Bumper b : carInject.bumpers) assertNotNull(b.uuid);
        Set<String> bumperUids = new HashSet<>(ListUtils.format(carInject.bumpers, it -> it.uuid));
        assertEquals(2, bumperUids.size());
        for (Wheel w : carInject.wheels) assertNotNull(w.uuid);
        Set<String> wheelsUid = new HashSet<>(ListUtils.format(carInject.wheels, it -> it.uuid));
        assertEquals(5, wheelsUid.size());
        for (Window w : carInject.windows) assertNotNull(w.uuid);
        Set<String> windowUids = new HashSet<>(ListUtils.format(carInject.windows, it -> it.uuid));
        assertEquals(4, windowUids.size());
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
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
        for (int i = 0; i < 2; i++) assertEquals(carInject1.bumpers.get(i).uuid, carInject2.bumpers.get(i).uuid);
        for (int i = 0; i < 2; i++) assertEquals(carInject1.wheels.get(i).uuid, carInject2.wheels.get(i).uuid);
        Set<String> windowUids = new HashSet<>();
        for (int i = 0; i < 4; i++) windowUids.add(carInject1.windows.get(i).uuid);
        for (int i = 0; i < 4; i++) windowUids.add(carInject2.windows.get(i).uuid);
        assertEquals(8, windowUids.size());
    }


    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    void carInjectProviderTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInjectProvider carInject = new CarInjectProvider();
        DI.inject(carInject);

        // Then
        assertEquals(0, Bumper.createCount, "Provide non used");
        assertNotNull(carInject.bumper.get().uuid);
        assertNotNull(carInject.wheel.get().uuid);
        assertNotNull(carInject.window.get().getValue().uuid);
        assertEquals(2, Bumper.createCount, "Bumpers created from list");
    }


    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
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
        assertEquals(carInject1.bumper.get().uuid, carInject2.bumper.get().uuid, "should cache");
        assertEquals(carInject1.wheel.get().uuid, carInject2.wheel.get().uuid, "should cache ");
        assertNotEquals(carInject1.window.get().getValue().uuid, carInject2.window.get().getValue().uuid, "provide via Provider. No caching");
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused");
    }

}
