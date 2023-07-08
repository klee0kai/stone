package com.github.klee0kai.stone.test.lists.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WheelReusableGcTests {

    @Test
    public void gcAllTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcAll();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertNotEquals(wheelSoft1, wheelSoft2);
        assertNotEquals(wheelStrong1, wheelStrong2);
    }


    @Test
    public void gcWeakTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcWeak();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertEquals(wheelSoft1, wheelSoft2);
        assertEquals(wheelStrong1, wheelStrong2);
    }

    @Test
    public void gcSoftTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcSoft();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertNotEquals(wheelSoft1, wheelSoft2);
        assertEquals(wheelStrong1, wheelStrong2);
    }


    @Test
    public void gcStrongTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcStrong();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertEquals(wheelSoft1, wheelSoft2);
        assertNotEquals(wheelStrong1, wheelStrong2);
    }

    @Test
    public void gcWheelsTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcWheels();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertNotEquals(wheelSoft1, wheelSoft2);
        assertNotEquals(wheelStrong1, wheelStrong2);
    }

    @Test
    public void gcNothing() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcNothing();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertEquals(wheelSoft1, wheelSoft2);
        assertEquals(wheelStrong1, wheelStrong2);
    }

    @Test
    public void gcWindows() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcWindows();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertEquals(wheelSoft1, wheelSoft2);
        assertEquals(wheelStrong1, wheelStrong2);
    }

    @Test
    public void gcWindowsAndWheels() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        String wheelFactory1 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak1 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft1 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong1 = DI.wheelsModule().wheelStrong().get().uuid;

        //When
        DI.gcWindowsAndWheels();
        String wheelFactory2 = DI.wheelsModule().wheelFactory().get().uuid;
        String wheelWeak2 = DI.wheelsModule().wheelWeak().get().uuid;
        String wheelSoft2 = DI.wheelsModule().wheelSoft().get().uuid;
        String wheelStrong2 = DI.wheelsModule().wheelStrong().get().uuid;

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2);
        assertNotEquals(wheelWeak1, wheelWeak2);
        assertNotEquals(wheelSoft1, wheelSoft2);
        assertNotEquals(wheelStrong1, wheelStrong2);
    }
}
