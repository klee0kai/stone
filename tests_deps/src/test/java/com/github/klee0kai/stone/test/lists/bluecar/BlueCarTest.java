package com.github.klee0kai.stone.test.lists.bluecar;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.car.di.bestOf.blue.BlueCarComponent;
import com.github.klee0kai.test.car.di.bestOf.both.BothCarComponent;
import com.github.klee0kai.test.car.di.bestOf.red.RedCarComponent;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class BlueCarTest {

    public static long startTime = 0;

    @BeforeAll
    public static void measurePerfStart() {
        startTime = System.currentTimeMillis();
    }

    @AfterAll
    public static void measurePerfEnd() {
        long endTime = System.currentTimeMillis();
        long spendTime = endTime - startTime;
        System.out.println("test time: " + spendTime + " ms");
        assertTrue(spendTime < 500, "Spend time " + spendTime + " ms");
    }

    @BeforeEach
    public void init() {
        Bumper.createCount = 0;
        Window.createCount = 0;
        Wheel.createCount = 0;
        Car.createCount = 0;
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    public void blueCarComponent() {
        //Given
        BlueCarComponent DI = Stone.createComponent(BlueCarComponent.class);

        //When
        assertEquals(0, Car.createCount);
        Car blueCar = DI.blueCar().get();

        //Then
        assertEquals(1, Car.createCount);
        assertNotNull(blueCar.uuid);
        assertEquals(2, blueCar.bumpers.size(), "Blue Car use list bumper");
        assertEquals(5, blueCar.wheels.size(), "Blue Car use list wheel");
        assertEquals(4, blueCar.windows.size(), "Blue Car use list wheel");
    }


    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    public void createBlueCar() {
        //Given
        BothCarComponent DI = Stone.createComponent(BothCarComponent.class);

        //When
        assertEquals(0, Car.createCount);
        Car blueCar = DI.blueCar().get();

        //Then
        assertEquals(1, Car.createCount);
        assertNotNull(blueCar.uuid);
        assertEquals(2, blueCar.bumpers.size(), "Blue Car use list bumper");
        assertEquals(5, blueCar.wheels.size(), "Blue Car use list wheel");
        assertEquals(4, blueCar.windows.size(), "Blue Car use list wheel");
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    public void redCarComponent() {
        //Given
        RedCarComponent DI = Stone.createComponent(RedCarComponent.class);

        //When
        assertEquals(0, Car.createCount);
        Car redCar = DI.redCar().get();

        //Then
        assertEquals(1, Car.createCount);
        assertNotNull(redCar.uuid);
        assertEquals(1, redCar.bumpers.size(), "Red Car use single bumper");
        assertEquals(1, redCar.wheels.size(), "Red Car use single wheel");
        assertEquals(1, redCar.windows.size(), "Red Car use single wheel");
    }

    @RepeatedTest(100)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS)
    public void createRedCar() {
        //Given
        BothCarComponent DI = Stone.createComponent(BothCarComponent.class);

        //When
        assertEquals(0, Car.createCount);
        Car redCar = DI.redCar().get();

        //Then
        assertEquals(1, Car.createCount);
        assertNotNull(redCar.uuid);
        assertEquals(1, redCar.bumpers.size(), "Red Car use single bumper");
        assertEquals(1, redCar.wheels.size(), "Red Car use single wheel");
        assertEquals(1, redCar.windows.size(), "Red Car use single wheel");
    }



}
