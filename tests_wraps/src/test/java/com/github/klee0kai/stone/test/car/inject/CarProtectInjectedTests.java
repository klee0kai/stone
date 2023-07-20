package com.github.klee0kai.stone.test.car.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.StListUtils;
import com.github.klee0kai.test.car.di.inject.CarInjectComponent;
import com.github.klee0kai.test.car.model.CarInject;
import com.github.klee0kai.test.car.model.CarInjectLists;
import com.github.klee0kai.test.car.model.CarInjectProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CarProtectInjectedTests {

    @Test
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
        assertEquals(bumperUid, carInject.bumper.uuid, "Providing with caching");
        assertEquals(wheelUid, carInject.wheel.uuid, "Providing with caching");
        assertNotEquals(windowUid, carInject.window.uuid, "Providing without caching");
    }


    @Test
    void carProtectListInjectedTest() {
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInjectLists carInject = new CarInjectLists();
        DI.inject(carInject);
        List<String> bumperUids = StListUtils.format(carInject.bumpers, it -> it.uuid);
        List<String> wheelUids = StListUtils.format(carInject.wheels, it -> it.uuid);
        List<String> windowUids = StListUtils.format(carInject.windows, it -> it.uuid);
        DI.protect(carInject);
        carInject = null;
        System.gc();
        carInject = new CarInjectLists();
        DI.inject(carInject);


        // Then
        assertEquals(bumperUids, StListUtils.format(carInject.bumpers, it -> it.uuid), "Providing with caching");
        assertEquals(wheelUids, StListUtils.format(carInject.wheels, it -> it.uuid), "Providing with caching");
        assertNotEquals(windowUids, StListUtils.format(carInject.windows, it -> it.uuid), "Providing without caching");
    }

    @Test
    void carProtectProviderTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInjectProvider carInject = new CarInjectProvider();
        DI.inject(carInject);
        String bumperUid = carInject.bumper.get().uuid;
        String wheelUid = carInject.wheel.get().uuid;
        String windowUid = carInject.window.get().getValue().uuid;
        DI.protect(carInject);
        carInject = null;
        System.gc();
        carInject = new CarInjectProvider();
        DI.inject(carInject);


        // Then
        assertEquals(bumperUid, carInject.bumper.get().uuid, "Lazy Provider caching and can be protected");
        assertNotEquals(wheelUid, carInject.wheel.get().uuid, "Ref is non caching and non should protect");
        assertNotEquals(windowUid, carInject.window.get().getValue().uuid, "LazyProvide non support caching. Non should protect");
    }


}
