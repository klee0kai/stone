package com.github.klee0kai.stone.test.lists.cachecontrol.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BumperGcResusableTests {

    @Test
    public void gcAllTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcAll();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertNotEquals(bumperSoftUids1, bumperSoftUids2);
        assertNotEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcWeakTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcWeak();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertEquals(bumperSoftUids1, bumperSoftUids2);
        assertEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcSoftTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcSoft();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertNotEquals(bumperSoftUids1, bumperSoftUids2);
        assertEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcStrongTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcStrong();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertEquals(bumperSoftUids1, bumperSoftUids2);
        assertNotEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcBumpers() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcBumpers();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertNotEquals(bumperSoftUids1, bumperSoftUids2);
        assertNotEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcRedBumpers() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcRedBumpers();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertEquals(bumperSoftUids1, bumperSoftUids2);
        assertNotEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcRedBumpers2() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcRedBumpers2();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertEquals(bumperSoftUids1, bumperSoftUids2);
        assertNotEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcWheelsTest() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcWheels();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertEquals(bumperSoftUids1, bumperSoftUids2);
        assertEquals(bumperStrongUids1, bumperStrongUids2);
    }

    @Test
    public void gcNothing() {
        // Given
        CarGcComponent DI = Stone.createComponent(CarGcComponent.class);
        List<String> bumperFactoryUids1 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids1 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids1 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids1 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        //When
        DI.gcNothing();
        List<String> bumperFactoryUids2 = ListUtils.format(DI.bumpersModule().bumperFactory(), it -> it.uuid);
        List<String> bumperWeakUids2 = ListUtils.format(DI.bumpersModule().bumperWeak(), it -> it.uuid);
        List<String> bumperSoftUids2 = ListUtils.format(DI.bumpersModule().bumperSoft(), it -> it.uuid);
        List<String> bumperStrongUids2 = ListUtils.format(DI.bumpersModule().bumperStrong(), it -> it.uuid);

        // Then
        assertNotEquals(bumperFactoryUids1, bumperFactoryUids2);
        assertNotEquals(bumperWeakUids1, bumperWeakUids2);
        assertEquals(bumperSoftUids1, bumperSoftUids2);
        assertEquals(bumperStrongUids1, bumperStrongUids2);
    }

}
