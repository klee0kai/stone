package com.github.klee0kai.test.tech;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;

public class PhoneStore {

    public static PhoneComponent DI = Stone.createComponent(PhoneComponent.class);


    public static void recreate() {
        DI = Stone.createComponent(PhoneComponent.class);
    }

}
