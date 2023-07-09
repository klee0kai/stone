package com.github.klee0kai.stone.test.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WindowPartialGcTests {

    @Test
    public void createWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);

        //When
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);

        //Then
        assertEquals(3, nonNullCount(windowStrong));
    }

    @Test
    public void holdInListTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowStrong = ListUtils.format(DI.windowsModule().windowStrong().get(), WeakReference::new);
        Window holder = DI.windowsModule().windowStrong().get().get(1);


        //When
        DI.gcAll();

        // Then
        assertNotNull(holder);
        assertEquals(1, nonNullCount(windowStrong));
        assertNotNull(windowStrong.get(1).get());
    }

    @Test
    public void partialRecreateList1Test() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> uids1 = ListUtils.format(DI.windowsModule().windowStrong().get(), it -> it.uuid);
        Window holder = DI.windowsModule().windowStrong().get().get(0);


        //When
        DI.gcAll();
        List<String> uids2 = ListUtils.format(DI.windowsModule().windowStrong().get(), it -> it.uuid);


        // Then
        assertNotNull(holder);
        assertEquals(uids1.get(0), uids2.get(0));
        assertNotEquals(uids1.get(1), uids2.get(1));
        assertNotEquals(uids1.get(2), uids2.get(2));
    }

    @Test
    public void partialRecreateList2Test() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> uids1 = ListUtils.format(DI.windowsModule().windowStrong().get(), it -> it.uuid);
        Window holder = DI.windowsModule().windowStrong().get().get(1);


        //When
        DI.gcAll();
        List<String> uids2 = ListUtils.format(DI.windowsModule().windowStrong().get(), it -> it.uuid);


        // Then
        assertNotNull(holder);
        assertNotEquals(uids1.get(0), uids2.get(0));
        assertEquals(uids1.get(1), uids2.get(1));
        assertNotEquals(uids1.get(2), uids2.get(2));
    }

    @Test
    public void partialRecreateList3Test() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> uids1 = ListUtils.format(DI.windowsModule().windowStrong().get(), it -> it.uuid);
        Window holder = DI.windowsModule().windowStrong().get().get(2);


        //When
        DI.gcAll();
        List<String> uids2 = ListUtils.format(DI.windowsModule().windowStrong().get(), it -> it.uuid);


        // Then
        assertNotNull(holder);
        assertNotEquals(uids1.get(0), uids2.get(0));
        assertNotEquals(uids1.get(1), uids2.get(1));
        assertEquals(uids1.get(2), uids2.get(2));
    }

    private int nonNullCount(List<WeakReference<Window>> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).get() != null) count++;
        }
        return count;
    }


}
