package com.github.klee0kai.stone.test.cache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.swcache.SwitchCacheComponent;
import com.github.klee0kai.test.mowgli.earth.Mountain;
import com.github.klee0kai.test.mowgli.earth.River;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EarthSwitchCacheTests {


    @Test
    public void allToWeakTest() {
        //Given
        SwitchCacheComponent DI = Stone.createComponent(SwitchCacheComponent.class);
        WeakReference<Mountain> mountain = new WeakReference<>(DI.earth().mountainStrong());

        //When
        DI.allWeak();
        System.gc();

        //Then
        assertNull(mountain.get());
    }

    @Test
    public void strongToWeakTest() {
        //Given
        SwitchCacheComponent DI = Stone.createComponent(SwitchCacheComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());

        //When
        DI.strongToWeak();
        System.gc();

        //Then
        assertNull(mountainStrong.get());
        assertNotNull(mountainSoft.get());
    }

    @Test
    public void weakToStrongFewMillisTest() throws InterruptedException {
        //Given
        SwitchCacheComponent DI = Stone.createComponent(SwitchCacheComponent.class);
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());

        //When
        DI.allStrongFewMillis();
        System.gc();

        //Then: can't GC
        assertNotNull(mountainWeak.get());

        //When: after few millis
        sleep(110);
        System.gc();

        //Then: can GC
        assertNull(mountainWeak.get());
    }


    @Test
    public void mountainToWeakTest() {
        //Given
        SwitchCacheComponent DI = Stone.createComponent(SwitchCacheComponent.class);
        WeakReference<Mountain> mountain = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<River> river = new WeakReference<>(DI.earth().riverSoft());

        //When
        DI.mountainToWeak();
        System.gc();

        //Then
        assertNull(mountain.get());
        assertNotNull(river.get());
    }


}
