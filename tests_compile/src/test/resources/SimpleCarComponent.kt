package com.github.klee0kai.stone.test;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;

@Component()
abstract class CarInjectComponent {

    abstract fun module(): CarInjectModule

}

@Module
abstract class CarInjectModule {

}
