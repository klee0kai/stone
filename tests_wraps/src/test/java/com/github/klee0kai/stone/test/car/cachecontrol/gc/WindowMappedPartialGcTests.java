package com.github.klee0kai.stone.test.car.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WindowMappedPartialGcTests {

    @Test
    public void createWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);

        //When
        List<WeakReference<Window>> windowStrong1 = ListUtils.format(DI.windowsMappedModule().windowStrong("1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong2 = ListUtils.format(DI.windowsMappedModule().windowStrong("2").get(), WeakReference::new);

        //Then
        assertEquals(3, nonNullCount(windowStrong1));
        assertEquals(3, nonNullCount(windowStrong2));
    }

    @Test
    public void holdInListTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Window>> windowStrong1 = ListUtils.format(DI.windowsMappedModule().windowStrong("1").get(), WeakReference::new);
        List<WeakReference<Window>> windowStrong2 = ListUtils.format(DI.windowsMappedModule().windowStrong("2").get(), WeakReference::new);
        Window holder1 = DI.windowsMappedModule().windowStrong("1").get().get(1);
        Window holder2 = DI.windowsMappedModule().windowStrong("2").get().get(0);


        //When
        DI.gcAll();

        // Then
        assertNotNull(holder1);
        assertNotNull(holder2);
        assertEquals(1, nonNullCount(windowStrong1));
        assertEquals(1, nonNullCount(windowStrong2));
        assertNotNull(windowStrong1.get(1).get());
        assertNotNull(windowStrong2.get(0).get());
    }

    @Test
    public void partialRecreateList1Test() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> uids1 = ListUtils.format(DI.windowsMappedModule().windowStrong("1").get(), it -> it.uuid);
        List<String> uids2 = ListUtils.format(DI.windowsMappedModule().windowStrong("2").get(), it -> it.uuid);
        List<String> uids3 = ListUtils.format(DI.windowsMappedModule().windowStrong("3").get(), it -> it.uuid);
        Window holder1 = DI.windowsMappedModule().windowStrong("1").get().get(0);
        Window holder2 = DI.windowsMappedModule().windowStrong("2").get().get(1);
        Window holder3 = DI.windowsMappedModule().windowStrong("3").get().get(2);


        //When
        DI.gcAll();
        List<String> uids1Reused = ListUtils.format(DI.windowsMappedModule().windowStrong("1").get(), it -> it.uuid);
        List<String> uids2Reused = ListUtils.format(DI.windowsMappedModule().windowStrong("2").get(), it -> it.uuid);
        List<String> uids3Reused = ListUtils.format(DI.windowsMappedModule().windowStrong("3").get(), it -> it.uuid);


        // Then
        assertNotNull(holder1);
        assertNotNull(holder2);
        assertNotNull(holder3);
        assertEquals(uids1.get(0), uids1Reused.get(0));
        assertNotEquals(uids1.get(1), uids1Reused.get(1));
        assertNotEquals(uids1.get(2), uids1Reused.get(2));

        assertNotEquals(uids2.get(0), uids2Reused.get(0));
        assertEquals(uids2.get(1), uids2Reused.get(1));
        assertNotEquals(uids2.get(2), uids2Reused.get(2));

        assertNotEquals(uids3.get(0), uids3Reused.get(0));
        assertNotEquals(uids3.get(1), uids3Reused.get(1));
        assertEquals(uids3.get(2), uids3Reused.get(2));
    }

    private int nonNullCount(List<WeakReference<Window>> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).get() != null) count++;
        }
        return count;
    }


}
