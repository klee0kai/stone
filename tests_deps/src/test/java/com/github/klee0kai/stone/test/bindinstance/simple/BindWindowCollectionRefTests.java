package com.github.klee0kai.stone.test.bindinstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BindWindowCollectionRefTests {
    @Test
    void bindWindowSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        Window window = new Window();
        DI.bindWindowRefs(Collections.singletonList(new WeakReference<>(window)));

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
        DI.bindWindowRefs(Arrays.asList(new WeakReference<>(window1), new WeakReference(window2)));

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
        DI.bindWindowRefs(Collections.singletonList(new WeakReference<>(window)));

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
        DI.bindWindowRefs(Arrays.asList(new WeakReference<>(window1), new WeakReference<>(window2)));

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
        DI.bindWindowRefs(Collections.singletonList(new WeakReference<>(new Window())));

        //When
        Window window = new Window();
        DI.bindWindowRefs(Collections.singletonList(new WeakReference<>(window)));

        //Then
        assertEquals(window.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window.uuid, DI.provideWindow().uuid);
        assertEquals(1, DI.provideWindows().size());
        assertEquals(window.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
    }

    @Test
    void unbindWindowEmptyListTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindWindowRefs(Arrays.asList(new WeakReference<>(new Window()), new WeakReference<>(new Window())));

        //When
        DI.bindWindowRefs(Collections.emptyList());

        //Then
        assertEquals(0, DI.module().windows().get().size());
        assertNull(DI.provideWindow());
        assertEquals(0, DI.provideWindows().size());
    }

    @Test
    void unbindWindowListRefTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindWindowRefs(Arrays.asList(new WeakReference<>(new Window()), new WeakReference<>(new Window())));

        //When
        DI.bindWindowRefs(Collections.singletonList(new WeakReference<>(null)));

        //Then
        assertNull(DI.provideWindow());
        assertEquals(0, DI.provideWindows().size());
    }


}
