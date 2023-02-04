package com.github.klee0kai.test.di.earthmirror;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.mowgli.earth.Cave;

@Component(
        qualifiers = {Cave.CaveType.class, Integer.class}
)
public interface EarthComponent {

    EastModule east();

    WestModule west();

}
