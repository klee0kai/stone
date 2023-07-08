package com.github.klee0kai.stone.test.lists.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.inject.CarInjectComponent;
import com.github.klee0kai.test.car.model.CarInject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CarProtectInjectedTests {

    @Test
    void carProtectInjectedTest() {
        //Given
        CarInjectComponent DI = Stone.createComponent(CarInjectComponent.class);

        //When
        CarInject carInject = new CarInject();
        DI.inject(carInject);
        String bumperUid = carInject.bumper.uuid;
        String wheelUid = carInject.wheel.uuid;
        String windowUid = carInject.window.uuid;
        DI.protect(carInject);
        carInject = null;
        System.gc();
        carInject = new CarInject();
        DI.inject(carInject);


        // Then
        assertEquals(bumperUid, carInject.bumper.uuid);
        assertEquals(wheelUid, carInject.wheel.uuid);
        assertNotEquals(windowUid, carInject.window.uuid);
    }

}
