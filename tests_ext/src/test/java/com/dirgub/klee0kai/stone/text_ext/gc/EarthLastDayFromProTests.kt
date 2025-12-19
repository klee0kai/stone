package com.dirgub.klee0kai.stone.text_ext.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.gcforest.GcGodComponent;
import com.github.klee0kai.test.mowgli.earth.Mountain;
import com.github.klee0kai.test.mowgli.earth.River;
import com.github.klee0kai.test_ext.inject.di.gcforest.GcGodExtComponent;
import com.github.klee0kai.test_ext.inject.mowgli.earth.Desert;
import com.github.klee0kai.test_ext.inject.mowgli.earth.WaterFlow;
import org.junit.jupiter.api.Test;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EarthLastDayFromProTests {

    @Test
    void gcAllTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());


        //When
        DIPro.gcAll();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainDef2, mountainWeak, mountainDef,
                riverStrong, riverSoft, riverWeak, riverDef,
                desertStrong, desertSoft, desertWeak, desertFactory,
                waterFlowStrong, waterFlowSoft, waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get());
        }
    }

    @Test
    void gcStrongTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcStrong();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainWeak, mountainDef, mountainDef2,
                riverStrong, riverWeak, riverDef,
                desertStrong, desertWeak, desertFactory,
                waterFlowStrong, waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get());
        }

        for (Reference ref : Arrays.asList(
                mountainSoft,
                riverSoft,
                desertSoft,
                waterFlowSoft
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcSoftTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcSoft();

        //Then
        for (Reference ref : Arrays.asList(
                mountainSoft, mountainWeak, mountainDef, mountainDef2,
                riverSoft, riverWeak, riverDef,
                desertSoft, desertWeak, desertFactory,
                waterFlowSoft, waterFlowDefRef, waterFlowWeak
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong,
                riverStrong,
                desertStrong,
                waterFlowStrong
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcWeakTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcWeak();

        //Then
        for (Reference ref : Arrays.asList(
                mountainWeak, mountainDef, mountainDef2,
                riverWeak, riverDef,
                desertWeak, desertFactory,
                waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft,
                riverStrong, riverSoft,
                desertStrong, desertSoft,
                waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcMountainTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcMountains();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainWeak, mountainDef, mountainDef2,
                riverWeak, riverDef,
                desertWeak, desertFactory,
                waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                riverStrong, riverSoft,
                desertStrong, desertSoft,
                waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcRiverTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcRivers();

        //Then
        for (Reference ref : Arrays.asList(
                mountainWeak, mountainDef, mountainDef2,
                riverStrong, riverSoft, riverWeak, riverDef,
                desertWeak, desertFactory,
                waterFlowStrong, waterFlowSoft, waterFlowDefRef, waterFlowWeak
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft,
                desertStrong, desertSoft
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcMountainAndRiverTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcMountainsAndRivers();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainSoft, mountainWeak, mountainDef, mountainDef2,
                riverStrong, riverSoft, riverWeak, riverDef,
                desertFactory, desertWeak,
                waterFlowStrong, waterFlowSoft, waterFlowDefRef, waterFlowWeak
        )) {
            assertNull(ref.get());
        }

        for (Reference ref : Arrays.asList(
                desertStrong, desertSoft
        )) {
            assertNotNull(ref.get());
        }
    }


    @Test
    void gcSoftMountainTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcSoftMountains();

        //Then
        for (Reference ref : Arrays.asList(
                mountainSoft, mountainWeak, mountainDef, mountainDef2,
                riverWeak, riverDef,
                desertFactory, desertWeak,
                waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainStrong,
                riverStrong, riverSoft,
                desertStrong, desertSoft,
                waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get());
        }
    }

    @Test
    void gcStrongMountainTest() {
        //Given
        GcGodComponent DI = Stone.createComponent(GcGodComponent.class);
        WeakReference<Mountain> mountainStrong = new WeakReference<>(DI.earth().mountainStrong());
        WeakReference<Mountain> mountainSoft = new WeakReference<>(DI.earth().mountainSoft());
        WeakReference<Mountain> mountainWeak = new WeakReference<>(DI.earth().mountainWeak());
        WeakReference<Mountain> mountainDef = new WeakReference<>(DI.earth().mountainDefaultFactory());
        WeakReference<Mountain> mountainDef2 = new WeakReference<>(DI.earth().mountainDefault2Factory());
        WeakReference<River> riverStrong = new WeakReference<>(DI.earth().riverStrong());
        WeakReference<River> riverSoft = new WeakReference<>(DI.earth().riverSoft());
        WeakReference<River> riverWeak = new WeakReference<>(DI.earth().riverWeak());
        WeakReference<River> riverDef = new WeakReference<>(DI.earth().riverDefaultSoft());
        GcGodExtComponent DIPro = Stone.createComponent(GcGodExtComponent.class);
        DIPro.extOf(DI);
        WeakReference<Desert> desertStrong = new WeakReference<>(DIPro.earth().desertStrong());
        WeakReference<Desert> desertSoft = new WeakReference<>(DIPro.earth().desertSoft());
        WeakReference<Desert> desertWeak = new WeakReference<>(DIPro.earth().desertWeak());
        WeakReference<Desert> desertFactory = new WeakReference<>(DIPro.earth().desertFactory());
        WeakReference<WaterFlow> waterFlowStrong = new WeakReference<>(DIPro.earth().riverStrong());
        WeakReference<WaterFlow> waterFlowSoft = new WeakReference<>(DIPro.earth().riverSoft());
        WeakReference<WaterFlow> waterFlowWeak = new WeakReference<>(DIPro.earth().riverWeak());
        WeakReference<WaterFlow> waterFlowDefRef = new WeakReference<>(DIPro.earth().riverDefaultSoft());

        //When
        DIPro.gcStrongMountains();

        //Then
        for (Reference ref : Arrays.asList(
                mountainStrong, mountainWeak, mountainDef, mountainDef2,
                riverWeak, riverDef,
                desertFactory, desertWeak,
                waterFlowWeak, waterFlowDefRef
        )) {
            assertNull(ref.get());
        }
        for (Reference ref : Arrays.asList(
                mountainSoft,
                riverStrong, riverSoft,
                desertStrong, desertSoft,
                waterFlowStrong, waterFlowSoft
        )) {
            assertNotNull(ref.get());
        }
    }

}
