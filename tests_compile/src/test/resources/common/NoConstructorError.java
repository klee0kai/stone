package com.github.klee0kai.simple.car;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;

@Component(
        identifiers = {Integer.class}
)
interface CarInjectComponent {

    CarInjectModule module1();


    SomeObject provideObject(Integer arg);

}


@Module
abstract class CarInjectModule {

    abstract SomeObject createObject(Integer arg);

}


class SomeObject {

    SomeObject(String arg) {

    }

}