package com.github.klee0kai.stone.test.boxed.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone._hidden_.types.StListUtils;
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

public class CarBoxedInjectMethodTests {

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
        assertNotNull(carInject.bumperFromMethod.val.uuid);
        assertNotNull(carInject.wheelFromMethod.val.uuid);
        assertNotNull(carInject.windowFromMethod.val.uuid);
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
        assertEquals(carInject1.bumperFromMethod.val.uuid, carInject2.bumperFromMethod.val.uuid, "should cache");
        assertEquals(carInject1.wheelFromMethod.val.uuid, carInject2.wheelFromMethod.val.uuid, "should cache ");
        assertNotEquals(carInject1.windowFromMethod.val.uuid, carInject2.windowFromMethod.val.uuid, "provide via Provider. No caching");
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
        assertEquals(8, Window.createCount, "Windows created for fields and method");
        assertEquals(2, carInject.bumpersMethodFrom.size());
        assertEquals(5, carInject.wheelsMethodFrom.size());
        assertEquals(4, carInject.windowsMethodFrom.size());
        for (CarBox<Bumper> b : carInject.bumpersMethodFrom) assertNotNull(b.val.uuid);
        Set<String> bumperUids = new HashSet<>(StListUtils.format(carInject.bumpersMethodFrom, it -> it.val.uuid));
        assertEquals(2, bumperUids.size());
        for (CarBox<Wheel> w : carInject.wheelsMethodFrom) assertNotNull(w.val.uuid);
        Set<String> wheelsUid = new HashSet<>(StListUtils.format(carInject.wheels, it -> it.val.uuid));
        assertEquals(5, wheelsUid.size());
        for (CarBox<Window> w : carInject.windowsMethodFrom) assertNotNull(w.val.uuid);
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
        assertEquals(2, carInject1.bumpersMethodFrom.size());
        assertEquals(5, carInject1.wheelsMethodFrom.size());
        assertEquals(4, carInject1.windowsMethodFrom.size());
        for (int i = 0; i < 2; i++)
            assertEquals(carInject1.bumpersMethodFrom.get(i).val.uuid, carInject2.bumpersMethodFrom.get(i).val.uuid);
        for (int i = 0; i < 2; i++)
            assertEquals(carInject1.wheelsMethodFrom.get(i).val.uuid, carInject2.wheelsMethodFrom.get(i).val.uuid);
        Set<String> windowUids = new HashSet<>();
        for (int i = 0; i < 4; i++) windowUids.add(carInject1.windowsMethodFrom.get(i).val.uuid);
        for (int i = 0; i < 4; i++) windowUids.add(carInject2.windowsMethodFrom.get(i).val.uuid);
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
        assertNotNull(carInject.bumperFromMethod.get().val.uuid);
        assertNotNull(carInject.wheelFromMethod.get().val.uuid);
        assertNotNull(carInject.windowFromMethod.get().getValue().val.uuid);
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
        assertEquals(carInject1.bumperFromMethod.get().val.uuid, carInject2.bumperFromMethod.get().val.uuid, "should cache");
        assertEquals(carInject1.wheelFromMethod.get().val.uuid, carInject2.wheelFromMethod.get().val.uuid, "should cache ");
        assertNotEquals(carInject1.windowFromMethod.get().getValue().val.uuid, carInject2.windowFromMethod.get().getValue().val.uuid, "provide via Provider. No caching");
        assertEquals(2, Bumper.createCount, "Bumpers created from list. And reused");
    }

}
