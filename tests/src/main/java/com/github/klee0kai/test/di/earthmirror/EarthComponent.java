package com.github.klee0kai.test.di.earthmirror;

import com.github.klee0kai.stone.annotations.component.Component;

@Component
public interface EarthComponent {

    EastModule east();

    WestModule west();

}
