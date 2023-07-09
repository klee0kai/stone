package com.github.klee0kai.stone.test.bindinstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent;
import com.github.klee0kai.test.car.model.Bumper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BindBumperTests {

    @Test
    void noBindTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //Then
        assertNull(DI.module().bumper());
        assertNull(DI.provideBumper().get());
        assertEquals(0, DI.provideBumpers().size());
    }


    @Test
    void nullBindTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        DI.bindBumper(null);

        //Then
        assertNull(DI.module().bumper());
        assertNull(DI.provideBumper().get());
        assertEquals(0, DI.provideBumpers().size());
    }

    @Test
    void bindWheelSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        Bumper bumper = new Bumper();
        DI.bindBumper(() -> bumper);

        //Then
        assertEquals(bumper.uuid, DI.module().bumper().get().uuid);
        assertEquals(bumper.uuid, DI.provideBumper().get().uuid);
        assertEquals(1, DI.provideBumpers().size());
        assertEquals(bumper.uuid, DI.provideBumpers().get(0).uuid);
    }

    @Test
    void rebindWheelSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindBumper(() -> new Bumper());

        //When
        Bumper bumper = new Bumper();
        DI.bindBumper(() -> bumper);

        //Then
        assertEquals(bumper.uuid, DI.module().bumper().get().uuid);
        assertEquals(bumper.uuid, DI.provideBumper().get().uuid);
        assertEquals(1, DI.provideBumpers().size());
        assertEquals(bumper.uuid, DI.provideBumpers().get(0).uuid);
    }

}
