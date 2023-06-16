package com.dirgub.klee0kai.stone.text_ext.cache;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.gcforest.GcGodComponent;
import com.github.klee0kai.test.mowgli.earth.Mountain;
import com.github.klee0kai.test_ext.inject.di.gcforest.GcGodExtComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EarthCacheAndProTests {


    @Test
    public void strongCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.__extOf(DI);

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
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.__extOf(DI);

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
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.__extOf(DI);

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
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.__extOf(DI);

        //When
        Mountain mountain1 = DI.earth().mountainDefaultSoft();
        Mountain mountain2 = DI.earth().mountainDefaultSoft();

        //Then
        assertEquals(
                mountain1.uuid,
                mountain2.uuid
        );
    }

    @Test
    public void differentMethodDifferentCacheTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.__extOf(DI);

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
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.__extOf(DI);

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
        GcGodExtComponent DIPro1 = Stone.createComponent(GcGodExtComponent.class);
        DIPro1.__extOf(DI1);
        GcGodComponent DI2 = Stone.createComponent(GcGodComponent.class);
        GcGodExtComponent DIPro2 = Stone.createComponent(GcGodExtComponent.class);
        DIPro2.__extOf(DI2);

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
