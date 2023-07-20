package com.github.klee0kai.stone.test.car.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone._hidden_.types.StListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WindowMultiMappedReusableGcTests {

    @Test
    public void createWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);

        //When
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //Then
        assertNotEquals(windowsFactory1, windowsFactory2);
        assertNotEquals(windowWeak1, windowWeak2);
        assertNotEquals(windowSoft1, windowSoft2);
        assertNotEquals(windowStrong1, windowStrong2);
    }

    @Test
    public void gcAllTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcAll();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertNotEquals(windowSoft1, windowSoft1Reuse);
        assertNotEquals(windowSoft2, windowSoft2Reuse);
        assertNotEquals(windowStrong1, windowStrong1Reuse);
        assertNotEquals(windowStrong2, windowStrong2Reuse);
    }

    @Test
    public void gcWeakTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcWeak();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertEquals(windowSoft1, windowSoft1Reuse);
        assertEquals(windowSoft2, windowSoft2Reuse);
        assertEquals(windowStrong1, windowStrong1Reuse);
        assertEquals(windowStrong2, windowStrong2Reuse);
    }

    @Test
    public void gcSoftTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcSoft();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertNotEquals(windowSoft1, windowSoft1Reuse);
        assertNotEquals(windowSoft2, windowSoft2Reuse);
        assertEquals(windowStrong1, windowStrong1Reuse);
        assertEquals(windowStrong2, windowStrong2Reuse);
    }

    @Test
    public void gcStrongTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcStrong();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertEquals(windowSoft1, windowSoft1Reuse);
        assertEquals(windowSoft2, windowSoft2Reuse);
        assertNotEquals(windowStrong1, windowStrong1Reuse);
        assertNotEquals(windowStrong2, windowStrong2Reuse);
    }

    @Test
    public void gcWindows() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcWindows();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertNotEquals(windowSoft1, windowSoft1Reuse);
        assertNotEquals(windowSoft2, windowSoft2Reuse);
        assertNotEquals(windowStrong1, windowStrong1Reuse);
        assertNotEquals(windowStrong2, windowStrong2Reuse);
    }

    @Test
    public void gcWindowsAndWheels() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcWindowsAndWheels();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertNotEquals(windowSoft1, windowSoft1Reuse);
        assertNotEquals(windowSoft2, windowSoft2Reuse);
        assertNotEquals(windowStrong1, windowStrong1Reuse);
        assertNotEquals(windowStrong2, windowStrong2Reuse);
    }

    @Test
    public void gcWheelsTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcWheels();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertEquals(windowSoft1, windowSoft1Reuse);
        assertEquals(windowSoft2, windowSoft2Reuse);
        assertEquals(windowStrong1, windowStrong1Reuse);
        assertEquals(windowStrong2, windowStrong2Reuse);
    }

    @Test
    public void gcNothing() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> windowsFactory1 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2 = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2 = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2 = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2 = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        //When
        DI.gcNothing();
        List<String> windowsFactory1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "1").get(), it -> it.uuid);
        List<String> windowWeak1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(1, "1").get(), it -> it.uuid);
        List<String> windowSoft1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(1, "1").get(), it -> it.uuid);
        List<String> windowStrong1Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "1").get(), it -> it.uuid);
        List<String> windowsFactory2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowFactory(1, "2").get(), it -> it.uuid);
        List<String> windowWeak2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowWeak(2, "1").get(), it -> it.uuid);
        List<String> windowSoft2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowSoft(2, "1").get(), it -> it.uuid);
        List<String> windowStrong2Reuse = StListUtils.format(DI.windowsMultiMappedModule().windowStrong(1, "2").get(), it -> it.uuid);

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse);
        assertNotEquals(windowsFactory2, windowsFactory2Reuse);
        assertNotEquals(windowWeak1, windowWeak1Reuse);
        assertNotEquals(windowWeak2, windowWeak2Reuse);
        assertEquals(windowSoft1, windowSoft1Reuse);
        assertEquals(windowSoft2, windowSoft2Reuse);
        assertEquals(windowStrong1, windowStrong1Reuse);
        assertEquals(windowStrong2, windowStrong2Reuse);
    }

}
