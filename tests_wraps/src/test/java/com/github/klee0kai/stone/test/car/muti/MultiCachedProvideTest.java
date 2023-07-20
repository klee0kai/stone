package com.github.klee0kai.stone.test.car.muti;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone._hidden_.types.StListUtils;
import com.github.klee0kai.stone.wrappers.Ref;
import com.github.klee0kai.test.car.di.lists.cached.CarMultiCachedComponent;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MultiCachedProvideTest {

    @BeforeEach
    public void init() {
        Bumper.createCount = 0;
        Window.createCount = 0;
        Wheel.createCount = 0;
        Car.createCount = 0;
    }

    @Test
    public void firstBumperFromCollection() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        assertEquals(0, Bumper.createCount);
        Ref<Bumper> bumperProvide = DI.singleBumper();

        //Then
        assertEquals(0, Bumper.createCount);
        Bumper bumper = bumperProvide.get();
        assertEquals(2, Bumper.createCount, "Bumper created via list");
        assertNotNull(bumper.uuid);
    }

    @Test
    public void cachedBumperFromCollection() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        Ref<Bumper> bumperProvide = DI.singleBumper();
        Bumper bumper1 = bumperProvide.get();
        Bumper bumper2 = bumperProvide.get();
        Bumper bumper3 = DI.singleBumper().get();

        //Then
        assertEquals(2, Bumper.createCount, "Bumper created via list. Cached");
        assertNotNull(bumper1.uuid);
        assertEquals(bumper1.uuid, bumper2.uuid);
        assertEquals(bumper1.uuid, bumper3.uuid);
    }

    @Test
    public void fourWheelsAndSpare() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        assertEquals(0, Wheel.createCount);
        List<Provider<WeakReference<Wheel>>> wheelsProviderList = DI.wheels();

        //Then
        // assertEquals(0, Wheel.createCount); // each item wrapping in list not delayed
        List<Wheel> wheels = StListUtils.format(wheelsProviderList, it -> it.get().get());
        assertEquals(5, Wheel.createCount);
        assertEquals(5, wheels.size());
        for (Wheel w : wheels) assertNotNull(w.uuid);
        Set<String> wheelsUid = new HashSet<>(StListUtils.format(wheels, it -> it.uuid));
        assertEquals(5, wheelsUid.size());
    }

    @Test
    public void fourWheelsAndSpareCached() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        List<Provider<WeakReference<Wheel>>> wheelsProviderList = DI.wheels();
        List<Wheel> wheels1 = StListUtils.format(wheelsProviderList, it -> it.get().get());
        List<Wheel> wheels2 = StListUtils.format(wheelsProviderList, it -> it.get().get());
        List<Wheel> wheels3 = StListUtils.format(DI.wheels(), it -> it.get().get());

        // Then
        assertEquals(5, Wheel.createCount, "wheel lists should cached");
        List<String> wheelsUuid1 = StListUtils.format(wheels1, it -> it.uuid);
        List<String> wheelsUuid2 = StListUtils.format(wheels2, it -> it.uuid);
        List<String> wheelsUuid3 = StListUtils.format(wheels3, it -> it.uuid);
        assertEquals(wheelsUuid1, wheelsUuid2);
        assertEquals(wheelsUuid1, wheelsUuid3);
    }

    @Test
    public void oneWheelFromList() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        Wheel wheel1 = DI.wheel();
        Wheel wheel2 = DI.wheel();

        //Then
        assertNotNull(wheel1.uuid);
        assertEquals(wheel1.uuid, wheel2.uuid);
    }

    @Test
    public void allWindowsInCar() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        assertEquals(0, Window.createCount);
        List<List<Window>> windows = DI.windows();

        //Then
        assertEquals(4, Window.createCount);
        assertEquals(1, windows.size());
        assertEquals(4, windows.get(0).size());
        for (Window w : windows.get(0)) assertNotNull(w.uuid);
        Set<String> windowUuid = new HashSet<>(StListUtils.format(windows.get(0), it -> it.uuid));
        assertEquals(4, windowUuid.size());
    }

    @Test
    public void allWindowsInCarFactory() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        assertEquals(0, Window.createCount);
        List<List<Window>> windows1 = DI.windows();
        List<List<Window>> windows2 = DI.windows();

        //Then
        assertEquals(8, Window.createCount, "Windows creating over provide wrappers. Should recreate each time");
    }

    @Test
    public void allWindowsInCarProvideWrapper() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        assertEquals(0, Window.createCount);
        List<Provider<List<Window>>> windows = DI.windowsProviding();

        //Then
        assertEquals(0, Window.createCount, "Provider non used.");
        assertEquals(1, windows.size());
        assertEquals(4, windows.get(0).get().size());
        assertEquals(4, Window.createCount, "Provider used. Windows created");
        for (Window w : windows.get(0).get()) assertNotNull(w.uuid);
        Set<String> windowUuid = new HashSet<>(StListUtils.format(windows.get(0).get(), it -> it.uuid));
        assertEquals(4, windowUuid.size());
    }

    @Test
    public void createCarsWithDeps() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        assertEquals(0, Car.createCount);
        List<Car> car = DI.cars();

        //Then
        assertEquals(2, Car.createCount);
        assertNotEquals(car.get(0).uuid, car.get(1).uuid);
        assertNotNull(car.get(0).bumpers);
        assertNotNull(car.get(0).wheels);
        assertNotNull(car.get(0).windows);
        assertNotNull(car.get(1).bumpers);
        assertNotNull(car.get(1).wheels);
        assertNotNull(car.get(1).windows);
        assertNotEquals(car.get(0).windows.size(), car.get(1).windows.size(), "Red use single deps and Blue car use listed deps");
    }

    @Test
    public void cacheCreatedCar() {
        //Given
        CarMultiCachedComponent DI = Stone.createComponent(CarMultiCachedComponent.class);

        //When
        assertEquals(0, Car.createCount);
        List<Car> car1 = DI.cars();
        List<Car> car2 = DI.cars();

        //Then
        assertEquals(2, Car.createCount);
        assertEquals(car1.size(), car2.size());
        assertEquals(car1.get(0).uuid, car2.get(0).uuid);
        assertEquals(car1.get(1).uuid, car2.get(1).uuid);
    }


}
