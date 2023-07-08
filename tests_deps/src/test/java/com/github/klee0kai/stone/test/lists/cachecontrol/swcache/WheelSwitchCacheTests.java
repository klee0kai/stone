package com.github.klee0kai.stone.test.lists.cachecontrol.swcache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.cachecontrol.swcache.CarSwCacheComponent;
import com.github.klee0kai.test.car.model.Wheel;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WheelSwitchCacheTests {

    @Test
    public void allWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.allWeak();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }

    @Test
    public void weakToStrongTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.weakToStrongFewMillis();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNotNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void weakToStrongAfterFewMillisTest() throws InterruptedException {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.weakToStrongFewMillis();
        Thread.sleep(150);
        System.gc();


        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void weakToSoftTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.weakToSoftFewMillis();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNotNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void weakToSoftAfterFewMillisTest() throws InterruptedException {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.weakToSoftFewMillis();
        Thread.sleep(150);
        System.gc();


        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void softToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.softToWeak();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void strongToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.strongToWeak();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }

    @Test
    public void wheelsToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.wheelsToWeak();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }

    @Test
    public void nothinToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.nothingToWeak();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void windowsToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.windowsToWeak();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNotNull(wheelSoft.get());
        assertNotNull(wheelStrong.get());
    }

    @Test
    public void windowAndWheelsToWeak() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        WeakReference<Wheel> wheelFactory = DI.wheelsModule().wheelFactory();
        WeakReference<Wheel> wheelWeak = DI.wheelsModule().wheelWeak();
        WeakReference<Wheel> wheelSoft = DI.wheelsModule().wheelSoft();
        WeakReference<Wheel> wheelStrong = DI.wheelsModule().wheelStrong();

        //When
        DI.windowsAndWheelsToWeak();
        System.gc();

        // Then
        assertNull(wheelFactory.get());
        assertNull(wheelWeak.get());
        assertNull(wheelSoft.get());
        assertNull(wheelStrong.get());
    }
}
