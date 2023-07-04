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

public class CarInjectMethodTests {

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
        assertNotNull(carInject.bumperFromMethod.uuid);
        assertNotNull(carInject.wheelFromMethod.uuid);
        assertNotNull(carInject.windowFromMethod.uuid);
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
        assertEquals(carInject1.bumperFromMethod.uuid, carInject2.bumperFromMethod.uuid, "should cache");
        assertEquals(carInject1.wheelFromMethod.uuid, carInject2.wheelFromMethod.uuid, "should cache ");
        assertNotEquals(carInject1.windowFromMethod.uuid, carInject2.windowFromMethod.uuid, "provide via Provider. No caching");
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
        assertEquals(8, Window.createCount, "Windows created for fields and method");
        assertEquals(2, carInject.bumpersMethodFrom.size());
        assertEquals(5, carInject.wheelsMethodFrom.size());
        assertEquals(4, carInject.windowsMethodFrom.size());
        for (Bumper b : carInject.bumpersMethodFrom) assertNotNull(b.uuid);
        Set<String> bumperUids = new HashSet<>(ListUtils.format(carInject.bumpersMethodFrom, it -> it.uuid));
        assertEquals(2, bumperUids.size());
        for (Wheel w : carInject.wheelsMethodFrom) assertNotNull(w.uuid);
        Set<String> wheelsUid = new HashSet<>(ListUtils.format(carInject.wheels, it -> it.uuid));
        assertEquals(5, wheelsUid.size());
        for (Window w : carInject.windowsMethodFrom) assertNotNull(w.uuid);
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
        assertEquals(2, carInject1.bumpersMethodFrom.size());
        assertEquals(5, carInject1.wheelsMethodFrom.size());
        assertEquals(4, carInject1.windowsMethodFrom.size());
        for (int i = 0; i < 2; i++)
            assertEquals(carInject1.bumpersMethodFrom.get(i).uuid, carInject2.bumpersMethodFrom.get(i).uuid);
        for (int i = 0; i < 2; i++)
            assertEquals(carInject1.wheelsMethodFrom.get(i).uuid, carInject2.wheelsMethodFrom.get(i).uuid);
        Set<String> windowUids = new HashSet<>();
        for (int i = 0; i < 4; i++) windowUids.add(carInject1.windowsMethodFrom.get(i).uuid);
        for (int i = 0; i < 4; i++) windowUids.add(carInject2.windowsMethodFrom.get(i).uuid);
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
        assertNotNull(carInject.bumperFromMethod.get().uuid);
        assertNotNull(carInject.wheelFromMethod.get().uuid);
        assertNotNull(carInject.windowFromMethod.get().getValue().uuid);
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
        assertEquals(carInject1.bumperFromMethod.get().uuid, carInject2.bumperFromMethod.get().uuid, "should cache");
        assertEquals(carInject1.wheelFromMethod.get().uuid, carInject2.wheelFromMethod.get().uuid, "should cache ");
        assertNotEquals(carInject1.windowFromMethod.get().getValue().uuid, carInject2.windowFromMethod.get().getValue().uuid, "provide via Provider. No caching");
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused");
    }

}
