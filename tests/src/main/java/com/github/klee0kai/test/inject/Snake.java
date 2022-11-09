package com.github.klee0kai.test.inject;

import com.github.klee0kai.stone.annotations.component.Inject;
import com.github.klee0kai.test.inject.forest.Blood;
import com.github.klee0kai.test.inject.forest.Earth;
import com.github.klee0kai.test.inject.forest.IAnimal;
import com.github.klee0kai.test.inject.identity.Conscience;
import com.github.klee0kai.test.inject.identity.Knowledge;

public class Snake implements IAnimal {

    @Inject
    public Blood blood;
    @Inject
    public Earth earth;
    @Inject
    public Conscience conscience;
    @Inject
    public Knowledge knowledge;

    @Override
    public void born() {
        Forest.DI.inject(this);
    }
}
