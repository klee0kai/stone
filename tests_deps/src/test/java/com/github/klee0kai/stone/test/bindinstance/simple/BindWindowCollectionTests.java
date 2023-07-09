package com.github.klee0kai.stone.test.bindinstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BindWindowCollectionTests {
    @Test
    void bindWindowSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        Window window = new Window();
        DI.bindWindows(Collections.singletonList(window));

        //Then
        assertEquals(window.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window.uuid, DI.provideWindow().uuid);
        assertEquals(1, DI.provideWindows().size());
        assertEquals(window.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
    }


    @Test
    void bindWindowListTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        Window window1 = new Window();
        Window window2 = new Window();
        DI.bindWindows(Arrays.asList(window1, window2));

        //Then
        assertEquals(window1.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window2.uuid, DI.module().windows().get().get(1).uuid);
        assertEquals(window1.uuid, DI.provideWindow().uuid);
        assertEquals(2, DI.provideWindows().size());
        assertEquals(window1.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
        assertEquals(window2.uuid, new ArrayList<>(DI.provideWindows()).get(1).uuid);
    }

    @Test
    void rebindWindowSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindWindow(new Window());

        //When
        Window window = new Window();
        DI.bindWindows(Collections.singletonList(window));

        //Then
        assertEquals(window.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window.uuid, DI.provideWindow().uuid);
        assertEquals(1, DI.provideWindows().size());
        assertEquals(window.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
    }

    @Test
    void rebindWindowListTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindWindow(new Window());

        //When
        Window window1 = new Window();
        Window window2 = new Window();
        DI.bindWindows(Arrays.asList(window1, window2));

        //Then
        assertEquals(window1.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window2.uuid, DI.module().windows().get().get(1).uuid);
        assertEquals(window1.uuid, DI.provideWindow().uuid);
        assertEquals(2, DI.provideWindows().size());
        assertEquals(window1.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
        assertEquals(window2.uuid, new ArrayList<>(DI.provideWindows()).get(1).uuid);
    }

    @Test
    void rebindWindowSimple2Test() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindWindows(Collections.singletonList(new Window()));

        //When
        Window window = new Window();
        DI.bindWindows(Collections.singletonList(window));

        //Then
        assertEquals(window.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window.uuid, DI.provideWindow().uuid);
        assertEquals(1, DI.provideWindows().size());
        assertEquals(window.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
    }

}
