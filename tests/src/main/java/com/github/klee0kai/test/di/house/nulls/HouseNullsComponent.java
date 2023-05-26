package com.github.klee0kai.test.di.house.nulls;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.house.House;

@Component
public interface HouseNullsComponent {

    HouseNullsModule module();

    House house();

}
