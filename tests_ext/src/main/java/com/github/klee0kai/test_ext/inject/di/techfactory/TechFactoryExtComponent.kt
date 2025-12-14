package com.github.klee0kai.test_ext.inject.di.techfactory;


import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.ExtendOf;
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent;
import com.github.klee0kai.test_ext.inject.di.techfactory.identifiers.Frequency;

@Component(
        identifiers = {Frequency.class}
)
public interface TechFactoryExtComponent extends TechFactoryComponent, ITechProviderExtComponent {

    @Override
    TechFactoryExtModule factory();

    @ExtendOf
    void extOf(TechFactoryComponent parent);

}
