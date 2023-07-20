package com.github.klee0kai.test.mowgli;

import com.github.klee0kai.stone.wrappers.LazyProvide;
import com.github.klee0kai.stone.wrappers.PhantomProvide;
import com.github.klee0kai.stone.wrappers.Ref;
import com.github.klee0kai.test.mowgli.community.History;
import com.github.klee0kai.test.mowgli.identity.Knowledge;

import javax.inject.Inject;
import javax.inject.Provider;

public class School {

    @Inject
    public LazyProvide<History> historyLazyProvide;

    @Inject
    public Ref<Knowledge> knowledgePhantomProvide2;

    @Inject
    public Provider<Knowledge> knowledgePhantomProvide3;

    @Inject
    public PhantomProvide<Knowledge> knowledgePhantomProvide;


}
