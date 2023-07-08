package com.github.klee0kai.stone.test.lists.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import com.github.klee0kai.test.car.model.Bumper;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BumperGcTests {

    @Test
    public void createWorkCorrect() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);

        //When
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //Then
        assertEquals(3, nonNullCount(bumperFactory));
        assertEquals(3, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }


    @Test
    public void gcAllTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcAll();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(0, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }


    @Test
    public void gcWeakTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcWeak();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }

    @Test
    public void gcSoftTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcSoft();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(0, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }


    @Test
    public void gcStrongTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcStrong();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void gcBumpers() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcBumpers();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(0, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void gcRedBumpers() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcRedBumpers();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void gcRedBumpers2() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcRedBumpers2();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(0, nonNullCount(bumperStrong));
    }

    @Test
    public void gcWheelsTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);


        //When
        DI.gcWheels();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }

    @Test
    public void gcNothing() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<WeakReference<Bumper>> bumperFactory = ListUtils.format(DI.bumpersModule().bumperFactory(), WeakReference::new);
        List<WeakReference<Bumper>> bumperWeak = ListUtils.format(DI.bumpersModule().bumperWeak(), WeakReference::new);
        List<WeakReference<Bumper>> bumperSoft = ListUtils.format(DI.bumpersModule().bumperSoft(), WeakReference::new);
        List<WeakReference<Bumper>> bumperStrong = ListUtils.format(DI.bumpersModule().bumperStrong(), WeakReference::new);

        //When
        DI.gcNothing();

        // Then
        assertEquals(0, nonNullCount(bumperFactory));
        assertEquals(0, nonNullCount(bumperWeak));
        assertEquals(3, nonNullCount(bumperSoft));
        assertEquals(3, nonNullCount(bumperStrong));
    }


    private int nonNullCount(List<WeakReference<Bumper>> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null && list.get(i).get() != null) count++;
        }
        return count;
    }


}
