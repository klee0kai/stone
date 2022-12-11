package com.github.klee0kai.stone.test.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.data.StoneRepository;
import com.github.klee0kai.test.gc.di.GComponent;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class GcScopesTests {


    @Test
    public void allScopeTest() {
        GComponent DI = Stone.createComponent(GComponent.class);

        WeakReference<StoneRepository> weakStone = new WeakReference<>(DI.data().provideWeak());
        WeakReference<StoneRepository> softStone = new WeakReference<>(DI.data().provideSoft());
        WeakReference<StoneRepository> strongStone = new WeakReference<>(DI.data().provideStrong());
        WeakReference<StoneRepository> soft2Stone = new WeakReference<>(DI.data().provideDefaultSoft());

        DI.gcAll();

        assertNull(weakStone.get());
        assertNull(softStone.get());
        assertNull(strongStone.get());
        assertNull(soft2Stone.get());
    }


    @Test
    public void weakScopeTest() {
        GComponent DI = Stone.createComponent(GComponent.class);

        WeakReference<StoneRepository> weakStone = new WeakReference<>(DI.data().provideWeak());
        WeakReference<StoneRepository> softStone = new WeakReference<>(DI.data().provideSoft());
        WeakReference<StoneRepository> strongStone = new WeakReference<>(DI.data().provideStrong());
        WeakReference<StoneRepository> soft2Stone = new WeakReference<>(DI.data().provideDefaultSoft());

        DI.gcWeak();

        assertNull(weakStone.get());
        assertNotNull(softStone.get());
        assertNotNull(strongStone.get());
        assertNotNull(soft2Stone.get());
    }


    @Test
    public void softScopeTest() {
        GComponent DI = Stone.createComponent(GComponent.class);

        WeakReference<StoneRepository> weakStone = new WeakReference<>(DI.data().provideWeak());
        WeakReference<StoneRepository> softStone = new WeakReference<>(DI.data().provideSoft());
        WeakReference<StoneRepository> strongStone = new WeakReference<>(DI.data().provideStrong());
        WeakReference<StoneRepository> soft2Stone = new WeakReference<>(DI.data().provideDefaultSoft());

        DI.gcSoft();

        assertNull(weakStone.get());
        assertNull(softStone.get());
        assertNotNull(strongStone.get());
        assertNull(soft2Stone.get());
    }


    @Test
    public void strongScopeTest() {
        GComponent DI = Stone.createComponent(GComponent.class);

        WeakReference<StoneRepository> weakStone = new WeakReference<>(DI.data().provideWeak());
        WeakReference<StoneRepository> softStone = new WeakReference<>(DI.data().provideSoft());
        WeakReference<StoneRepository> strongStone = new WeakReference<>(DI.data().provideStrong());
        WeakReference<StoneRepository> soft2Stone = new WeakReference<>(DI.data().provideDefaultSoft());

        DI.gcStrong();

        assertNull(weakStone.get());
        assertNotNull(softStone.get());
        assertNull(strongStone.get());
        assertNotNull(soft2Stone.get());
    }

}
