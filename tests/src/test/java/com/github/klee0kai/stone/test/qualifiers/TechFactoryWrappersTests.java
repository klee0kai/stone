package com.github.klee0kai.stone.test.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.types.IRef;
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent;
import com.github.klee0kai.test.tech.components.Battery;
import org.junit.jupiter.api.Test;

import javax.inject.Provider;
import java.lang.ref.Reference;

import static org.junit.jupiter.api.Assertions.*;

public class TechFactoryWrappersTests {

    @Test
    public void lazyProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        IRef<Battery> battery = DI.batteryLazy();

        //Then
        assertEquals(
                battery.get().uuid,
                battery.get().uuid
        );
    }

    @Test
    public void softRefProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        Reference<Battery> battery = DI.batterySoft();

        //Then
        assertNotNull(battery.get());
    }

    @Test
    public void weakRefProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        Reference<Battery> battery = DI.batteryWeak();

        //Then
        assertNotNull(battery.get());
    }

    @Test
    public void phantom1ProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        IRef<Battery> battery = DI.batteryPhantomProvider();

        //Then
        assertNotEquals(
                battery.get().uuid,
                battery.get().uuid
        );
    }

    @Test
    public void phantom2ProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        Provider<Battery> battery = DI.batteryProvider();

        //Then
        assertNotEquals(
                battery.get().uuid,
                battery.get().uuid
        );
    }

    @Test
    public void phantom3ProvideTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        IRef<Battery> battery = DI.batteryProviderIRef();

        //Then
        assertNotEquals(
                battery.get().uuid,
                battery.get().uuid
        );
    }


}
