package com.github.klee0kai.test_ext.inject.di.techfactory;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.di.techfactory.TechFactoryModule;
import com.github.klee0kai.test_ext.inject.di.techfactory.qualifiers.Frequency;
import com.github.klee0kai.test_ext.inject.tech.components.DDR3Ram;

import javax.inject.Named;

@Module
public interface TechFactoryExtModule extends TechFactoryModule {

    @Override
    @Named()
    @Provide(cache = Provide.CacheType.Factory)
    DDR3Ram ram();

    @Override
    @Named("size")
    @Provide(cache = Provide.CacheType.Factory)
    DDR3Ram ram(RamSize ramSize);

    @Named("size-frequency")
    @Provide(cache = Provide.CacheType.Factory)
    DDR3Ram ram(RamSize ramSize, Frequency frequency);

}
