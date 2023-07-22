package com.github.klee0kai.simple.car;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;

@Component
interface CarInjectComponent {

    @Provide
    Object module();


}


@Module
abstract class CarInjectModule {

}
