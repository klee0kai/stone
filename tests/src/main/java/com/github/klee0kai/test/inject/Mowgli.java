package com.github.klee0kai.test.inject;

import com.github.klee0kai.stone.annotations.component.Inject;
import com.github.klee0kai.stone.types.LazyProvide;
import com.github.klee0kai.stone.types.PhantomProvide;
import com.github.klee0kai.test.inject.forest.Blood;
import com.github.klee0kai.test.inject.forest.Earth;
import com.github.klee0kai.test.inject.forest.History;
import com.github.klee0kai.test.inject.forest.IAnimal;
import com.github.klee0kai.test.inject.identity.Conscience;
import com.github.klee0kai.test.inject.identity.Knowledge;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import static com.github.klee0kai.test.inject.Forest.DI;

public class Mowgli implements IAnimal {

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
    public WeakReference<Knowledge> knowledgeWeakRef;

    @Inject
    public SoftReference<Knowledge> knowledgeSoftRef;

    @Inject
    public LazyProvide<Knowledge> knowledgeLazyProvide;

    @Inject
    public PhantomProvide<Knowledge> knowledgePhantomProvide;

    @Override
    public void born() {
        DI.inject(this);
    }
}
