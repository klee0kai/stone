package com.github.klee0kai.wiki.wrapping;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.wrappers.LazyProvide;
import com.github.klee0kai.stone.wrappers.PhantomProvide;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.Ram;

import javax.inject.Inject;

public class GoodPhone {

    static TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

    @Inject
    public PhantomProvide<Battery> battery;

    @Inject
    public LazyProvide<Ram> ram;

    public void create() {
        DI.inject(this);
    }

}
