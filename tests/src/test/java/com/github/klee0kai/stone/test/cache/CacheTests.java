package com.github.klee0kai.stone.test.cache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.cache.di.CacheTestComponent;
import com.github.klee0kai.test.data.StoneRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CacheTests {

    @Test
    public void simpleCacheTest() {
        CacheTestComponent DI = Stone.createComponent(CacheTestComponent.class);

        StoneRepository repFactory1 = DI.data().provideFactory();
        StoneRepository repFactory2 = DI.data().provideFactory();
        StoneRepository repFactory3 = DI.data().provideFactory();

        assertNotEquals(repFactory1.uuid, repFactory2.uuid);
        assertNotEquals(repFactory2.uuid, repFactory3.uuid);

        StoneRepository repStrong1 = DI.data().provideStrong();
        StoneRepository repStrong2 = DI.data().provideStrong();
        StoneRepository repStrong3 = DI.data().provideStrong();

        assertNotEquals(repFactory1.uuid, repStrong1.uuid);
        assertEquals(repStrong1.uuid, repStrong2.uuid);
        assertEquals(repStrong2.uuid, repStrong3.uuid);


        StoneRepository repSoft1 = DI.data().provideSoft();
        StoneRepository repSoft2 = DI.data().provideSoft();
        StoneRepository repSoft3 = DI.data().provideSoft();

        assertNotEquals(repFactory1.uuid, repStrong1.uuid);
        assertNotEquals(repStrong1.uuid, repSoft1.uuid);
        assertEquals(repSoft1.uuid, repSoft2.uuid);
        assertEquals(repSoft2.uuid, repSoft3.uuid);


        StoneRepository repWeak1 = DI.data().provideWeak();
        StoneRepository repWeak2 = DI.data().provideWeak();
        StoneRepository repWeak3 = DI.data().provideWeak();

        assertNotEquals(repWeak1.uuid, repStrong1.uuid);
        assertNotEquals(repWeak2.uuid, repSoft1.uuid);
        assertEquals(repWeak1.uuid, repWeak2.uuid);
        assertEquals(repWeak2.uuid, repWeak3.uuid);

        StoneRepository repDefSoft1 = DI.data().provideDefaultSoft();
        StoneRepository repDefSoft2 = DI.data().provideDefaultSoft();
        StoneRepository repDefSoft3 = DI.data().provideDefaultSoft();

        assertNotEquals(repDefSoft1.uuid, repStrong1.uuid);
        assertNotEquals(repDefSoft1.uuid, repSoft1.uuid);
        assertEquals(repDefSoft1.uuid, repDefSoft2.uuid);
        assertEquals(repDefSoft2.uuid, repDefSoft3.uuid);
    }

    @Test
    public void refLifecycleTest(){
        CacheTestComponent DI = Stone.createComponent(CacheTestComponent.class);

    }

    @Test
    public void separatedDITTest(){
        CacheTestComponent DI1 = Stone.createComponent(CacheTestComponent.class);
        CacheTestComponent DI2 = Stone.createComponent(CacheTestComponent.class);
    }

}
