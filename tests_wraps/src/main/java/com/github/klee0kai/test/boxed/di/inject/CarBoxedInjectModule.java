package com.github.klee0kai.test.boxed.di.inject;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.test.boxed.model.CarBox;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Module
public class CarBoxedInjectModule {

    @Provide(cache = Provide.CacheType.Weak)
    public List<CarBox<Wheel>> fourWheels() {
        return Arrays.asList(
                new CarBox<>(new Wheel()),
                new CarBox<>(new Wheel()),
                new CarBox<>(new Wheel()),
                new CarBox<>(new Wheel())
        );
    }

    @Provide(cache = Provide.CacheType.Weak)
    public CarBox<Wheel> spareWheel() {
        return new CarBox<>(new Wheel());
    }

    @Provide(cache = Provide.CacheType.Factory)
    public Provider<CarBox<Window>> frontWindow() {
        return () -> new CarBox<>(new Window());
    }

    @Provide(cache = Provide.CacheType.Factory)
    public Provider<WeakReference<CarBox<Window>>> backWindow() {
        return () -> new WeakReference<>(new CarBox<>(new Window()));
    }

    @Provide(cache = Provide.CacheType.Factory)
    public List<Ref<CarBox<Window>>> passengerWindows() {
        return Arrays.asList(() -> new CarBox<>(new Window()), () -> new CarBox<>(new Window()));
    }

    @Provide(cache = Provide.CacheType.Weak)
    public Collection<CarBox<Bumper>> bumpers() {
        return Arrays.asList(new CarBox<>(new Bumper()), new CarBox<>(new Bumper()));
    }

}
