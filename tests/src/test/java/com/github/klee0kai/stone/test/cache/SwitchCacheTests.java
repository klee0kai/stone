package com.github.klee0kai.stone.test.cache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.cache.di.CacheTestComponent;
import com.github.klee0kai.test.data.StoneRepository;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class SwitchCacheTests {

    @Test
    public void strongToWeakTest() {
        CacheTestComponent DI = Stone.createComponent(CacheTestComponent.class);

        WeakReference<StoneRepository> repStrongRef = new WeakReference<>(DI.data().provideStrong());

        System.gc();
        assertNotNull(repStrongRef.get());

        DI.allWeak();

        System.gc();
        assertNull(repStrongRef.get());
    }

    @Test
    public void strongToWeak2Test() {
        CacheTestComponent DI = Stone.createComponent(CacheTestComponent.class);

        WeakReference<StoneRepository> repStrongRef = new WeakReference<>(DI.data().provideStrong());
        WeakReference<StoneRepository> repSoftRef = new WeakReference<>(DI.data().provideSoft());

        System.gc();
        assertNotNull(repStrongRef.get());
        assertNotNull(repSoftRef.get());

        DI.strongToWeak();

        System.gc();
        assertNull(repStrongRef.get());
        assertNotNull(repSoftRef.get());

        DI.allWeak();

        System.gc();
        assertNull(repSoftRef.get());
    }

    @Test
    public void weakToStrongFewMillisTest() throws InterruptedException {
        CacheTestComponent DI = Stone.createComponent(CacheTestComponent.class);

        WeakReference<StoneRepository> repWeakRef = new WeakReference<>(DI.data().provideWeak());
        assertNotNull(repWeakRef.get());

        DI.allStrongFewMillis();

        System.gc();
        System.gc();
        assertNotNull(repWeakRef.get());

        sleep(110);

        System.gc();
        assertNull(repWeakRef.get());
    }


}
