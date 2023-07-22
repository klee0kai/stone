package com.github.klee0kai.simple.car;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;

import javax.inject.Inject;

@Component
interface CarInjectComponent {

    CarInjectModule module();


}


@Module
abstract class CarInjectModule {

    @Inject
    String someProvide();

}
