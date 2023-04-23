package com.github.klee0kai.test.di.house.simple;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.house.House;

@Component
public interface HouseComponent {

    HouseModule module();

    RoomsModule rooms();

    AreasModule area();

    ToolsModule tools();

    House house();

}
