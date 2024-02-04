package com.github.klee0kai.wiki.provide.binding;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

@Component
public interface SunSystemComponent {

    @BindInstance
    Sun sun(Sun sun);

}
