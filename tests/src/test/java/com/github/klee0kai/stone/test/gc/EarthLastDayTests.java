package com.github.klee0kai.stone.test.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.gcforest.GcGodComponent;
import com.github.klee0kai.test.mowgli.earth.Mountain;
import com.github.klee0kai.test.mowgli.earth.River;
import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EarthLastDayTests {

    @Test
    void gcAllTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcAll();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainWeak, mountainDef,
                riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get());
        }
    }

    @Test
    void gcStrongTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcStrong();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainWeak,
                riverStrong, riverWeak
        )) {
            assertNull(ref.get());
        }

        for (Reference ref : Arrays.asList(
                mountainSoft, mountainDef,
                riverSoft, riverDef
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcSoftTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcSoft();

        //Then
        for (Reference ref : Arrays.asList(
                mountainSoft, mountainWeak, mountainDef,
                riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong,
                riverStrong
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcWeakTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcWeak();

        //Then
        for (Reference ref : Arrays.asList(
                mountainWeak,
                riverWeak
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainDef,
                riverStrong, riverSoft, riverDef
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcMountainTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcMountains();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainWeak, mountainDef,
                riverWeak
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                riverStrong, riverSoft, riverDef
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcRiverTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcRivers();

        //Then
        for (Reference ref : Arrays.asList(
                mountainWeak,
                riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainDef
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcMountainAndRiverTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcMountainsAndRivers();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainWeak, mountainDef,
                riverStrong, riverSoft, riverWeak, riverDef
        )) {
            assertNull(ref.get());
        }
    }


    @Test
    void gcSoftMountainTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcSoftMountains();

        //Then
        for (Reference ref : Arrays.asList(
                mountainSoft, mountainWeak, mountainDef,
                riverWeak
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong,
                riverStrong, riverSoft, riverDef
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcStrongMountainTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(di.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(di.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(di.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(di.earth().mountainDefaultSoft());
        WeakReference<River> riverStrong = new WeakReference<>(di.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(di.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(di.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(di.earth().riverDefaultSoft());

        //When
        di.gcStrongMountains();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainWeak,
                riverWeak
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainSoft, mountainDef,
                riverStrong, riverSoft, riverDef
        )) {
            assertNotNull(ref.get());
        }
    }

}
