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
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = di.earth().mountainStrong();
        Mountain mountain2 = di.earth().mountainStrong();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }


    @Test
    public void softCacheTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = di.earth().mountainSoft();
        Mountain mountain2 = di.earth().mountainSoft();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

    @Test
    public void weakCacheTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = di.earth().mountainWeak();
        Mountain mountain2 = di.earth().mountainWeak();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

    @Test
    public void defCacheTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = di.earth().mountainDefaultSoft();
        Mountain mountain2 = di.earth().mountainDefaultSoft();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

    @Test
    public void differentMethodDifferentCacheTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountainStrong = di.earth().mountainStrong();
        Mountain mountainSoft = di.earth().mountainSoft();

        //Then
        assertNotEquals(
                mountainStrong.uuid,
                mountainSoft.uuid
        );
    }


    @Test
    public void factoryNotCacheTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = di.earth().mountainFactory();
        Mountain mountain2 = di.earth().mountainFactory();

        //Then
        assertNotEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }


    @Test
    public void differentDIDifferentCacheTest() {
        //Given
        GcGodComponent di1 = Stone.createComponent(GcGodComponent.class);
        GcGodComponent di2 = Stone.createComponent(GcGodComponent.class);

        //When
        Mountain mountain1 = di1.earth().mountainStrong();
        Mountain mountain2 = di2.earth().mountainStrong();

        //Then
        assertNotEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

}
