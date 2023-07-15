package com.github.klee0kai.test.car.di.qualifiers;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifier;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierMulti;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.MyQualifierWithString;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Car;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import javax.inject.Named;
import javax.inject.Provider;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

@Module
public class CarQCModule {

    @Provide(cache = Provide.CacheType.Factory)
    public Car carSimple(Wheel wheel, Bumper bumper, Window window) {
        return new Car(bumper, wheel, window);
    }

    @Provide(cache = Provide.CacheType.Factory)
    public Car carProvider(Provider<Bumper> bumper, Provider<Wheel> wheel, Provider<Window> window) {
        return new Car(bumper.get(), wheel.get(), window.get());
    }

    @Provide(cache = Provide.CacheType.Factory)
    public Car carRef(WeakReference<Bumper> bumper, Reference<Wheel> wheel, Reference<Window> window) {
        return new Car(bumper.get(), wheel.get(), window.get());
    }


    @Provide(cache = Provide.CacheType.Factory)
    public Car carList(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        return new Car(bumper, wheel, window);
    }

    @Named()
    @Provide(cache = Provide.CacheType.Factory)
    public Car carNamedEmpty(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "named_empty";
        return car;
    }


    @Named("a")
    @Provide(cache = Provide.CacheType.Factory)
    public Car carNamedA(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "named_a";
        return car;
    }


    @MyQualifier
    @Provide(cache = Provide.CacheType.Factory)
    public Car carMyQualifier(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "my_qualifier";
        return car;
    }


    @MyQualifierWithString()
    @Provide(cache = Provide.CacheType.Factory)
    public Car carIdQualifier(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "my_qualifier_with_string";
        return car;
    }


    @MyQualifierWithString(id = "a")
    @Provide(cache = Provide.CacheType.Factory)
    public Car carIdQualifierA(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "my_qualifier_a";
        return car;
    }


    @MyQualifierWithString(id = "b")
    @Provide(cache = Provide.CacheType.Factory)
    public Car carIdQualifierB(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "my_qualifier_b";
        return car;
    }


    @MyQualifierMulti()
    @Provide(cache = Provide.CacheType.Factory)
    public Car carQualifierMulti(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "qualifier_multi";
        return car;
    }


    @MyQualifierMulti(id = "a", indx = 1)
    @Provide(cache = Provide.CacheType.Factory)
    public Car carQualifierMultiA1(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "qualifier_multi_a1";
        return car;
    }

    @MyQualifierMulti(id = "a", indx = 2)
    @Provide(cache = Provide.CacheType.Factory)
    public Car carQualifierMultiA2(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "qualifier_multi_a2";
        return car;
    }


    @MyQualifierMulti(id = "a", indx = 2, type = MyQualifierMulti.Type.HARD)
    @Provide(cache = Provide.CacheType.Factory)
    public Car carQualifierMultiA2Hard(List<Bumper> bumper, List<Wheel> wheel, List<Window> window) {
        Car car = new Car(bumper, wheel, window);
        car.qualifier = "qualifier_multi_a2_hard";
        return car;
    }


}
