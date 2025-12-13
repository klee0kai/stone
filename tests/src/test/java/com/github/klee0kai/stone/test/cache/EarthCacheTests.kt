package com.github.klee0kai.stone.test.cache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.gcforest.GcGodComponent;
import com.github.klee0kai.test.mowgli.earth.Mountain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EarthCacheTests {


    @Test
    public void strongCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = DI.earth().mountainStrong();
        Mountain mountain2 = DI.earth().mountainStrong();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }


    @Test
    public void softCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = DI.earth().mountainSoft();
        Mountain mountain2 = DI.earth().mountainSoft();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

    @Test
    public void weakCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = DI.earth().mountainWeak();
        Mountain mountain2 = DI.earth().mountainWeak();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

    @Test
    public void defCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = DI.earth().mountainDefaultFactory();
        Mountain mountain2 = DI.earth().mountainDefaultFactory();

        //Then
        assertNotEquals(
                mountain1.uuid,
                mountain2.uuid,
                "Default is factory providing"
        );
    }

    @Test
    public void defCache2Test() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = DI.earth().mountainDefault2Factory();
        Mountain mountain2 = DI.earth().mountainDefault2Factory();

        //Then
        assertNotEquals(
                mountain1.uuid,
                mountain2.uuid,
                "Default is factory providing"
        );
    }

    @Test
    public void differentMethodDifferentCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountainStrong = DI.earth().mountainStrong();
        Mountain mountainSoft = DI.earth().mountainSoft();

        //Then
        assertNotEquals(
                mountainStrong.uuid,
                mountainSoft.uuid
        );
    }


    @Test
    public void factoryNotCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = DI.earth().mountainFactory();
        Mountain mountain2 = DI.earth().mountainFactory();

        //Then
        assertNotEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }


    @Test
    public void differentDIDifferentCacheTest() {
        //Given
        GcGodComponent DI1 = Stone.createComponent(GcGodComponent.class);
        GcGodComponent DI2 = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = DI1.earth().mountainStrong();
        Mountain mountain2 = DI2.earth().mountainStrong();

        //Then
        assertNotEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

}
