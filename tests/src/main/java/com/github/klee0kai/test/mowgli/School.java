package com.github.klee0kai.test.mowgli;

import com.github.klee0kai.stone.types.IRef;
import com.github.klee0kai.stone.types.LazyProvide;
import com.github.klee0kai.stone.types.PhantomProvide;
import com.github.klee0kai.test.mowgli.world.History;
import com.github.klee0kai.test.mowgli.identity.Knowledge;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.github.klee0kai.test.mowgli.Forest.DI;

public class School {

    @Inject
    public LazyProvide<History> historyLazyProvide;

    @Inject
    public IRef<Knowledge> knowledgePhantomProvide2;

    @Inject
    public Provider<Knowledge> knowledgePhantomProvide3;

    @Inject
    public PhantomProvide<Knowledge> knowledgePhantomProvide;


    public void build() {
        DI.inject(this);
    }

}
