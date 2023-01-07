package com.github.klee0kai.test.di.techfactory;

import com.github.klee0kai.stone.annotations.component.Component;

@Component
public abstract class TechFactoryComponent {

    public abstract TechFactoryModule factory();

}
