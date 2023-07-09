package com.github.klee0kai.stone.test.bindinstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent;
import com.github.klee0kai.test.car.model.Wheel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BindWheelTests {

    @Test
    void noBindTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //Then
        assertNull(DI.module().wheel());
        assertNull(DI.provideWheel());
        assertNull(DI.provideWheelRef());
        assertEquals(0, DI.provideWheels().size());
    }


    @Test
    void nullBindTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        DI.bindWheel(null);

        //Then
        assertNull(DI.module().wheel());
        assertNull(DI.provideWheel());
        assertNull(DI.provideWheelRef());
        assertEquals(0, DI.provideWheels().size());
    }

    @Test
    void bindWheelSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);

        //When
        Wheel wheel = new Wheel();
        DI.bindWheel(wheel);

        //Then
        assertEquals(wheel.uuid, DI.module().wheel().uuid);
        assertEquals(wheel.uuid, DI.provideWheel().uuid);
        assertEquals(wheel.uuid, DI.provideWheelRef().get().uuid);
        assertEquals(1, DI.provideWheels().size());
        assertEquals(wheel.uuid, DI.provideWheels().get(0).get().uuid);
    }

    @Test
    void rebindWheelSimpleTest() {
        //Given
        CarBindComponent DI = Stone.createComponent(CarBindComponent.class);
        DI.bindWheel(new Wheel());

        //When
        Wheel wheel = new Wheel();
        DI.bindWheel(wheel);

        //Then
        assertEquals(wheel.uuid, DI.module().wheel().uuid);
        assertEquals(wheel.uuid, DI.provideWheel().uuid);
        assertEquals(wheel.uuid, DI.provideWheelRef().get().uuid);
        assertEquals(1, DI.provideWheels().size());
        assertEquals(wheel.uuid, DI.provideWheels().get(0).get().uuid);
    }

}
