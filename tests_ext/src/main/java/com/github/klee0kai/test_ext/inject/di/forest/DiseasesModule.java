package com.github.klee0kai.test_ext.inject.di.forest;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test_ext.inject.mowgli.diseases.Osteoarthritis;

@Module
abstract public class DiseasesModule {

    @Provide
    abstract public Osteoarthritis osteoarthritis();

}
