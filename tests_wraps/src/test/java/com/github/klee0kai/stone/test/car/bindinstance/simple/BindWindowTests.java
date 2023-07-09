package com.github.klee0kai.stone.test.car.bindinstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BindWindowTests {

    @Test
    void noBindTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //Then
        assertNull(DI.module().windows());
        assertNull(DI.provideWindow());
        assertEquals(0, DI.provideWindows().size());
    }


    @Test
    void nullBindTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        DI.bindWindow(null);

        //Then
        assertNull(DI.module().windows());
        assertNull(DI.provideWindow());
        assertEquals(0, DI.provideWindows().size());
    }

    @Test
    void bindWindowSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        Window window = new Window();
        DI.bindWindow(window);

        //Then
        assertEquals(window.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window.uuid, DI.provideWindow().uuid);
        assertEquals(1, DI.provideWindows().size());
        assertEquals(window.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
    }

    @Test
    void rebindWindowSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindWindow(new Window());

        //When
        Window window = new Window();
        DI.bindWindow(window);

        //Then
        assertEquals(window.uuid, DI.module().windows().get().get(0).uuid);
        assertEquals(window.uuid, DI.provideWindow().uuid);
        assertEquals(1, DI.provideWindows().size());
        assertEquals(window.uuid, new ArrayList<>(DI.provideWindows()).get(0).uuid);
    }


}
