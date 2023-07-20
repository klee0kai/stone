package com.github.klee0kai.stone.test.car.cachecontrol.swcache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone._hidden_.types.StListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.swcache.CarSwCacheComponent;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WindowMultiMappedSwitchCacheTests {

    @Test
    public void allWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);


        //When
        DI.allWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void weakToStrongTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.weakToStrongFewMillis();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(3, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void weakToStrongAfterFewMillisTest() throws InterruptedException {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.weakToStrongFewMillis();
        Thread.sleep(150);
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void weakToSoftTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.weakToSoftFewMillis();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(3, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void weakToSoftAfterFewMillisTest() throws InterruptedException {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.weakToSoftFewMillis();
        Thread.sleep(150);
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void softToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.softToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void strongToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.strongToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void windowsToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.windowsToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void windowAndWheelsToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.windowsAndWheelsToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void wheelToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);


        //When
        DI.wheelsToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void weakNothingTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Window>> windowsFactory = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), WeakReference::new);

        //When
        DI.nothingToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    private int nonNullCount(List<WeakReference<Window>> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).get() != null) count++;
        }
        return count;
    }

}
