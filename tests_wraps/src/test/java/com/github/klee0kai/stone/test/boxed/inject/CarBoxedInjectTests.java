package com.github.klee0kai.stone.test.boxed.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.StListUtils;
import com.github.klee0kai.test.boxed.di.inject.CarBoxedInjectComponent;
import com.github.klee0kai.test.boxed.model.CarBox;
import com.github.klee0kai.test.boxed.model.CarBoxedInject;
import com.github.klee0kai.test.boxed.model.CarBoxedInjectLists;
import com.github.klee0kai.test.boxed.model.CarBoxedInjectProvider;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CarBoxedInjectTests {

    @BeforeEach
    public void init() {
        Bumper.createCount = 0;
        Window.createCount = 0;
        Wheel.createCount = 0;
        Car.createCount = 0;
    }

    @Test
    void carInjectTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInject carInject = new CarBoxedInject();
        DI.inject(carInject);

        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list");
        assertNotNull(carInject.bumper.val.uuid);
        assertNotNull(carInject.wheel.val.uuid);
        assertNotNull(carInject.window.val.uuid);
    }

    @Test
    void carInjectReusableTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInject carInject1 = new CarBoxedInject();
        CarBoxedInject carInject2 = new CarBoxedInject();
        DI.inject(carInject1);
        DI.inject(carInject2);


        // Then
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused");
        assertEquals(carInject1.bumper.val.uuid, carInject2.bumper.val.uuid, "should cache");
        assertEquals(carInject1.wheel.val.uuid, carInject2.wheel.val.uuid, "should cache ");
        assertNotEquals(carInject1.window.val.uuid, carInject2.window.val.uuid, "provide via Provider. No caching");
    }


    @Test
    void carInjectListTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInjectLists carInject = new CarBoxedInjectLists();
        DI.inject(carInject);

        // Then
        assertEquals(2, Bumper.createCount);
        assertEquals(5, Wheel.createCount);
        assertEquals(8, Window.createCount, "Window created for fields and method");
        assertEquals(2, carInject.bumpers.size());
        assertEquals(5, carInject.wheels.size());
        assertEquals(4, carInject.windows.size());
        for (CarBox<Bumper> b : carInject.bumpers) assertNotNull(b.val.uuid);
        Set<String> bumperUids = new HashSet<>(StListUtils.format(carInject.bumpers, it -> it.val.uuid));
        assertEquals(2, bumperUids.size());
        for (CarBox<Wheel> w : carInject.wheels) assertNotNull(w.val.uuid);
        Set<String> wheelsUid = new HashSet<>(StListUtils.format(carInject.wheels, it -> it.val.uuid));
        assertEquals(5, wheelsUid.size());
        for (CarBox<Window> w : carInject.windows) assertNotNull(w.val.uuid);
        Set<String> windowUids = new HashSet<>(StListUtils.format(carInject.windows, it -> it.val.uuid));
        assertEquals(4, windowUids.size());
    }

    @Test
    void carInjectListReusableTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInjectLists carInject1 = new CarBoxedInjectLists();
        CarBoxedInjectLists carInject2 = new CarBoxedInjectLists();
        DI.inject(carInject1);
        DI.inject(carInject2);

        // Then
        assertEquals(2, carInject1.bumpers.size());
        assertEquals(5, carInject1.wheels.size());
        assertEquals(4, carInject1.windows.size());
        for (int i = 0; i < 2; i++)
            assertEquals(carInject1.bumpers.get(i).val.uuid, carInject2.bumpers.get(i).val.uuid);
        for (int i = 0; i < 2; i++) assertEquals(carInject1.wheels.get(i).val.uuid, carInject2.wheels.get(i).val.uuid);
        Set<String> windowUids = new HashSet<>();
        for (int i = 0; i < 4; i++) windowUids.add(carInject1.windows.get(i).val.uuid);
        for (int i = 0; i < 4; i++) windowUids.add(carInject2.windows.get(i).val.uuid);
        assertEquals(8, windowUids.size());
    }


    @Test
    void carInjectProviderTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInjectProvider carInject = new CarBoxedInjectProvider();
        DI.inject(carInject);

        // Then
        assertEquals(0, Bumper.createCount, "Provide non used");
        assertNotNull(carInject.bumper.get().val.uuid);
        assertNotNull(carInject.wheel.get().val.uuid);
        assertNotNull(carInject.window.get().getValue().val.uuid);
        assertEquals(2, Bumper.createCount, "Bumpers created from list");
    }


    @Test
    void carInjectProvideReusableTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInjectProvider carInject1 = new CarBoxedInjectProvider();
        CarBoxedInjectProvider carInject2 = new CarBoxedInjectProvider();
        DI.inject(carInject1);
        DI.inject(carInject2);


        // Then
        assertEquals(0, Bumper.createCount, "Provider non used");
        assertEquals(carInject1.bumper.get().val.uuid, carInject2.bumper.get().val.uuid, "should cache");
        assertEquals(carInject1.wheel.get().val.uuid, carInject2.wheel.get().val.uuid, "should cache ");
        assertNotEquals(carInject1.window.get().getValue().val.uuid, carInject2.window.get().getValue().val.uuid, "provide via Provider. No caching");
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused");
    }

}
