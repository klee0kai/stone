package com.github.klee0kai.test.car.di.qualifiers;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.wrappers.Ref;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.BumperQualifier;
import com.github.klee0kai.test.car.di.qualifiers.qualifiers.WheelCount;
import com.github.klee0kai.test.car.model.Bumper;
import com.github.klee0kai.test.car.model.Wheel;
import com.github.klee0kai.test.car.model.Window;

import java.util.Arrays;
import java.util.List;

@Module
public abstract class CarQPModule {

    @Provide(cache = Provide.CacheType.Factory)
    public abstract Wheel wheel();

    @WheelCount(count = 4)
    @Provide(cache = Provide.CacheType.Factory)
    public List<Wheel> fourWheel() {
        return Arrays.asList(new Wheel(), new Wheel(), new Wheel(), new Wheel());
    }

    @BumperQualifier(type = BumperQualifier.BumperType.Simple)
    @Provide(cache = Provide.CacheType.Factory)
    public Bumper bumperSimple() {
        Bumper bumper = new Bumper();
        bumper.qualifier = "simple";
        return bumper;
    }

    @BumperQualifier(type = BumperQualifier.BumperType.Reinforced)
    @Provide(cache = Provide.CacheType.Factory)
    public Bumper bumper() {
        Bumper bumper = new Bumper();
        bumper.qualifier = "reinforced";
        return bumper;
    }

    @Provide(cache = Provide.CacheType.Factory)
    public abstract Ref<List<Window>> windows();

}
