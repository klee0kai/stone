package com.github.klee0kai.stone.test.lists.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import com.github.klee0kai.test.car.model.Wheel;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WheelMappedGcTests {

    @Test
    public void createWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);

        //When
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //Then
        assertNotNull(wheelFactory.get());
        assertNotNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void createAfterGcWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        DI.gcAll();

        //When
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //Then
        assertNotNull(wheelFactory.get());
        assertNotNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void gcAllTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcAll();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }

    @Test
    public void gcWeakTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcWeak();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void gcSoftTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcSoft();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void gcStrongTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcStrong();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }

    @Test
    public void gcWheelsTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcWheels();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }

    @Test
    public void gcNothing() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcNothing();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void gcWindows() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcWindows();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void gcWindowsAndWheels() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelMappedModule().wheelFactory("1");
        WeakReference<Wheel> wheelWeak = DI.wheelMappedModule().wheelWeak("1");
        WeakReference<Wheel> wheelSoft = DI.wheelMappedModule().wheelSoft("1");
        WeakReference<Wheel> wheelStrong = DI.wheelMappedModule().wheelStrong("1");

        //When
        DI.gcWindowsAndWheels();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }
}
