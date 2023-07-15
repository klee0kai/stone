package com.github.klee0kai.test.car.di.lists.factory;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Named;
import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Module
public abstract class CarMultiModule {

    @Provide(cache = Provide.CacheType.Factory)
    public List<Wheel> fourWheels() {
        return Arrays.asList(new Wheel(), new Wheel(), new Wheel(), new Wheel());
    }

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    public abstract Wheel spareWheel();

    @Provide(cache = Provide.CacheType.Factory)
    public abstract Provider<Window> frontWindow();

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    public abstract Provider<WeakReference<Window>> backWindow();

    @Named
    @Provide(cache = Provide.CacheType.Factory)
    public List<Ref<Window>> passengerWindows() {
        return Arrays.asList(Window::new, Window::new);
    }

    @Provide(cache = Provide.CacheType.Factory)
    public Collection<Bumper> bumpers() {
        return Arrays.asList(new Bumper(), new Bumper());
    }

    @Named("redCar")
    @Provide(cache = Provide.CacheType.Factory)
    public abstract Car redCar(Bumper bumper, Wheel wheel, Window window);

    @Named("blueCar")
    @Provide(cache = Provide.CacheType.Factory)
    public abstract Car blueCar(List<Bumper> bumpers, List<Wheel> wheels, List<Window> windows);


}
