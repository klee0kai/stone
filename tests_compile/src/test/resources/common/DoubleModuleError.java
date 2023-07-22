package com.github.klee0kai.simple.car;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;

@Component()
abstract class CarInjectComponent {

    protected abstract CarInjectModule module1();

    protected abstract CarInjectModule module2();

}


@Module
abstract class CarInjectModule {

}
