package com.github.klee0kai.stone.test.boxed.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.test.boxed.di.inject.CarBoxedInjectComponent;
import com.github.klee0kai.test.boxed.model.CarBoxedInject;
import com.github.klee0kai.test.boxed.model.CarBoxedInjectLists;
import com.github.klee0kai.test.boxed.model.CarBoxedInjectProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CarBoxedProtectInjectedTests {

    @Test
    void carProtectInjectedTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInject carInject = new CarBoxedInject();
        DI.inject(carInject);
        String bumperUid = carInject.bumper.val.uuid;
        String wheelUid = carInject.wheel.val.uuid;
        String windowUid = carInject.window.val.uuid;
        DI.protect(carInject);
        carInject = null;
        System.gc();
        carInject = new CarBoxedInject();
        DI.inject(carInject);


        // Then
        assertEquals(bumperUid, carInject.bumper.val.uuid, "Providing with caching");
        assertEquals(wheelUid, carInject.wheel.val.uuid, "Providing with caching");
        assertNotEquals(windowUid, carInject.window.val.uuid, "Providing without caching");
    }


    @Test
    void carProtectListInjectedTest() {
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInjectLists carInject = new CarBoxedInjectLists();
        DI.inject(carInject);
        List<String> bumperUids = ListUtils.format(carInject.bumpers, it -> it.val.uuid);
        List<String> wheelUids = ListUtils.format(carInject.wheels, it -> it.val.uuid);
        List<String> windowUids = ListUtils.format(carInject.windows, it -> it.val.uuid);
        DI.protect(carInject);
        carInject = null;
        System.gc();
        carInject = new CarBoxedInjectLists();
        DI.inject(carInject);


        // Then
        assertEquals(bumperUids, ListUtils.format(carInject.bumpers, it -> it.val.uuid), "Providing with caching");
        assertEquals(wheelUids, ListUtils.format(carInject.wheels, it -> it.val.uuid), "Providing with caching");
        assertNotEquals(windowUids, ListUtils.format(carInject.windows, it -> it.val.uuid), "Providing without caching");
    }

    @Test
    void carProtectProviderTest() {
        //Given
        CarBoxedInjectComponent DI = Stone.createComponent(CarBoxedInjectComponent.class);

        //When
        CarBoxedInjectProvider carInject = new CarBoxedInjectProvider();
        DI.inject(carInject);
        String bumperUid = carInject.bumper.get().val.uuid;
        String wheelUid = carInject.wheel.get().val.uuid;
        String windowUid = carInject.window.get().getValue().val.uuid;
        DI.protect(carInject);
        carInject = null;
        System.gc();
        carInject = new CarBoxedInjectProvider();
        DI.inject(carInject);


        // Then
        assertEquals(bumperUid, carInject.bumper.get().val.uuid, "Lazy Provider caching and can be protected");
        assertNotEquals(wheelUid, carInject.wheel.get().val.uuid, "Ref is non caching and non should protect");
        assertNotEquals(windowUid, carInject.window.get().getValue().val.uuid, "LazyProvide non support caching. Non should protect");
    }


}
