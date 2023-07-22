package com.github.klee0kai.simple.car;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;

@Component(identifiers = {String.class})
interface CarInjectComponent {

    CarInjectModule module();

    String sayHelloProvide();

}


@Module
abstract class CarInjectModule {

    abstract String sayHello(String dep);

}
