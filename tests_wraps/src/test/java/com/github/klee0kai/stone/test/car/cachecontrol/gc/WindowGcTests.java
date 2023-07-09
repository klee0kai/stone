package com.github.klee0kai.stone.test.car.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WindowGcTests {

    @Test
    public void createWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);

        //When
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //Then
        assertEquals(3, nonNullCount(windowsFactory));
        assertEquals(3, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void createAfterGcWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        DI.gcAll();

        //When
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //Then
        assertEquals(3, nonNullCount(windowsFactory));
        assertEquals(3, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void gcAllTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);


        //When
        DI.gcAll();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void gcWeakTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //When
        DI.gcWeak();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void gcSoftTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //When
        DI.gcSoft();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void gcStrongTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //When
        DI.gcStrong();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void gcWindows() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //When
        DI.gcWindows();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void gcWindowsAndWheels() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //When
        DI.gcWindowsAndWheels();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(0, nonNullCount(windowSoft));
        assertEquals(0, nonNullCount(windowStrong));
    }

    @Test
    public void gcWheelsTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);


        //When
        DI.gcWheels();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void gcNothing() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowsFactory = ListUtils.format(DI.windowsModule().windowFactory().get(), WeakReference::new);
        List<WeakReference<Window>> windowWeak = ListUtils.format(DI.windowsModule().windowWeak().get(), WeakReference::new);
        List<WeakReference<Window>> windowSoft = ListUtils.format(DI.windowsModule().windowSoft().get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //When
        DI.gcNothing();

        // Then
        assertEquals(0, nonNullCount(windowsFactory));
        assertEquals(0, nonNullCount(windowWeak));
        assertEquals(3, nonNullCount(windowSoft));
        assertEquals(3, nonNullCount(windowStrong));
    }


    private int nonNullCount(List<WeakReference<Window>> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).get() != null) count++;
        }
        return count;
    }


}
