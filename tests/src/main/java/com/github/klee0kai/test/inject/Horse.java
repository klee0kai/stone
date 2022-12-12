package com.github.klee0kai.test.inject;

import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleListener;
import com.github.klee0kai.stone.types.lifecycle.IStoneLifeCycleOwner;
import com.github.klee0kai.test.inject.forest.Blood;
import com.github.klee0kai.test.inject.forest.Earth;
import com.github.klee0kai.test.inject.forest.History;
import com.github.klee0kai.test.inject.forest.IAnimal;
import com.github.klee0kai.test.inject.identity.Conscience;
import com.github.klee0kai.test.inject.identity.Ideology;
import com.github.klee0kai.test.inject.identity.Knowledge;

import javax.inject.Inject;

public class Horse implements IAnimal {

    @Inject
    public Blood blood;
    @Inject
    public Earth earth;
    @Inject
    public History history;
    @Inject
    public Conscience conscience;
    @Inject
    public Knowledge knowledge;
    @Inject
    public Ideology ideology;

    @Override
    public void born() {
        Forest.DI.inject(this, listener -> {

        });
    }
}
