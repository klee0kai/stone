package com.dirgub.klee0kai.stone.text_ext.cache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.swcache.SwitchCacheComponent;
import com.github.klee0kai.test.mowgli.earth.Mountain;
import com.github.klee0kai.test.mowgli.earth.River;
import com.github.klee0kai.test_ext.inject.di.swcache.SwitchCacheExtComponent;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExtEarthSwitchCacheTests {


    @Test
    public void allToWeakTest() {
        //Given
        SwitchCacheComponent DI = Stone.createComponent(SwitchCacheComponent.class);
        SwitchCacheExtComponent DIPro = Stone.createComponent(SwitchCacheExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Mountain> mountain = new WeakReference<>(DIPro.earth().mountainStrong());

        //When
        DIPro.allWeakExt();
        System.gc();

        //Then
        assertNull(mountain.get());
    }

    @Test
    public void strongToWeakTest() {
        //Given
        SwitchCacheComponent DI = Stone.createComponent(SwitchCacheComponent.class);
        SwitchCacheExtComponent DIPro = Stone.createComponent(SwitchCacheExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DIPro.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DIPro.earth().mountainSoft());

        //When
        DIPro.strongToWeakExt();
        System.gc();

        //Then
        assertNull(mountainStrong.get());
        assertNotNull(mountainSoft.get());
    }

    @Test
    public void weakToStrongFewMillisTest() throws InterruptedException {
        //Given
        SwitchCacheComponent DI = Stone.createComponent(SwitchCacheComponent.class);
        SwitchCacheExtComponent DIPro = Stone.createComponent(SwitchCacheExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DIPro.earth().mountainWeak());

        //When
        DIPro.allStrongFewMillisExt();
        System.gc();

        //Then: can't GC
        assertNotNull(mountainWeak.get());

        //When: after few millis
        sleep(110);
        System.gc();

        //Then: can GC
        assertNull(mountainWeak.get());
    }





}