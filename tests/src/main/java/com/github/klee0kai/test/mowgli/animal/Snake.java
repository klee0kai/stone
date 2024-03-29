package com.github.klee0kai.test.mowgli.animal;

import com.github.klee0kai.test.mowgli.body.Blood;
import com.github.klee0kai.test.mowgli.community.History;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.identity.Conscience;
import com.github.klee0kai.test.mowgli.identity.Knowledge;

import javax.inject.Inject;

public class Snake implements IAnimal {

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

}
