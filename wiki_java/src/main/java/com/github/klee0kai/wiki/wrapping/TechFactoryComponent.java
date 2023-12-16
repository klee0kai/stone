package com.github.klee0kai.wiki.wrapping;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.wrappers.LazyProvide;
import com.github.klee0kai.stone.wrappers.PhantomProvide;
import com.github.klee0kai.test.tech.components.Battery;
import com.github.klee0kai.test.tech.components.Ram;

@Component(wrapperProviders = {CustomWrapper.class})
public interface TechFactoryComponent {

    TechFactoryModule factory();

    PhantomProvide<Battery> battery();

    LazyProvide<Ram> ramMemory();

    void inject(GoodPhone goodPhone);
}
