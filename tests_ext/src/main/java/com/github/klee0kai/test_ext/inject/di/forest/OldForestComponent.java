package com.github.klee0kai.test_ext.inject.di.forest;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.di.base_forest.ForestComponent;
import com.github.klee0kai.test_ext.inject.mowgli.animal.OldHorse;

@Component
public interface OldForestComponent extends ForestComponent {


    @Override
    OldIdentityModule identity();

    DiseasesModule diseases();


    void inject(OldHorse horse, IStoneLifeCycleOwner iStoneLifeCycleOwner);


}
