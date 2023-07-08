package com.github.klee0kai.stone.test.lists.cachecontrol.swcache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.swcache.CarSwCacheComponent;
import com.github.klee0kai.test.car.model.Bumper;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BumperSwitchCacheTests {

    @Test
    public void allWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.allWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(0, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void weakToStrongTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.weakToStrongFewMillis();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(3, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }

    @Test
    public void weakToStrongAfterFewMillisTest() throws InterruptedException {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.weakToStrongFewMillis();
        Thread.sleep(150);
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }

    @Test
    public void softToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.softToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(0, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }

    @Test
    public void strongToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.strongToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void bumpersToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.bumpersToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(0, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void redBumpersToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.redBumpersToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void redBumpersToWeak2Test() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.redBumpersToWeak2();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void wheelsToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);


        //When
        DI.wheelsToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }

    @Test
    public void nothingToWeakTest() {
        // Given
        CarSwCacheComponent DI = Stone.createComponent(CarSwCacheComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.nothingToWeak();
        System.gc();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }

    private int nonNullCount(List<WeakReference<Bumper>> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).get() != null) count++;
        }
        return count;
    }

}
