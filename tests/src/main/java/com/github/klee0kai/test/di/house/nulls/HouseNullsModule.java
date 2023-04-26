package com.github.klee0kai.test.di.house.nulls;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.house.House;
import com.github.klee0kai.test.house.kitchen.Kichen;

@Module
public class HouseNullsModule {

    public Kichen kichen() {
        return new Kichen(null, null, null);
    }

    public House house(Kichen kichen) {
        return new House(kichen, null, null, null);
    }

}
