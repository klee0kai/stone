package com.github.klee0kai.test_ext.inject.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test_ext.inject.diseases.Osteoarthritis;

@Module
abstract public class DiseasesModule {

    @Provide
    abstract public Osteoarthritis osteoarthritis();

}
