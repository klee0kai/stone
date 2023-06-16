package com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.ExtendOf;
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent;

@Component
public interface ExtPlanetComponent extends PlanetComponent {

    @ExtendOf
    void extOf(PlanetComponent parent);

}
