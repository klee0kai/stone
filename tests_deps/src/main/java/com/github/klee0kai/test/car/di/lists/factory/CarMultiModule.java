package com.github.klee0kai.test.car.di.lists.factory;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.types.wrappers.Ref;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Provider;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Module
public abstract class CarMultiModule {

    public List<Wheel> fourWheels() {
        return Arrays.asList(new Wheel(), new Wheel(), new Wheel(), new Wheel());
    }

    public abstract Wheel spareWheel();

    public abstract Provider<Window> frontWindow();

    public abstract Provider<WeakReference<Window>> backWindow();

    public List<Ref<Window>> passengerWindows() {
        return Arrays.asList(Window::new, Window::new);
    }

    public Collection<Bumper> bumpers() {
        return Arrays.asList(new Bumper(), new Bumper());
    }

    public abstract Car redCar(Bumper bumper, Wheel wheel, Window window);

    public abstract Car blueCar(List<Bumper> bumpers, List<Wheel> wheels, List<Window> windows);


}
