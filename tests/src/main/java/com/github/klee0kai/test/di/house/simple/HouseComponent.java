package com.github.klee0kai.test.di.house.simple;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.test.house.House;
import com.github.klee0kai.test.house.InHouse;
import com.github.klee0kai.test.house.kitchen.storagearea.Sanitizers;
import com.github.klee0kai.test.house.identifiers.StoreAreaType;

@Component(
        identifiers = {StoreAreaType.class}
)
public interface HouseComponent {

    HouseModule module();

    RoomsModule rooms();

    AreasModule area();

    ToolsModule tools();

    House house();

    Sanitizers sanitizers();

    House house(StoreAreaType type);

    void inject(StoreAreaType storeAreaType,InHouse inHouse);

    void inject(InHouse inHouse);

}
