package com.github.klee0kai.test_ext.inject.di.techfactory;


import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.test.di.techfactory.TechFactoryComponent;
import com.github.klee0kai.test_ext.inject.di.techfactory.qualifiers.Frequency;

@Component(
        qualifiers = {Frequency.class}
)
public interface TechFactoryExtComponent extends TechFactoryComponent, ITechProviderExtComponent, IComponent {

    @Override
    TechFactoryExtModule factory();

}
