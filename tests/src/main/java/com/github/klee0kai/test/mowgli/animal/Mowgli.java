package com.github.klee0kai.test.mowgli.animal;

import com.github.klee0kai.stone.types.IRef;
import com.github.klee0kai.stone.types.LazyProvide;
import com.github.klee0kai.stone.types.PhantomProvide;
import com.github.klee0kai.test.mowgli.body.Blood;
import com.github.klee0kai.test.mowgli.world.Earth;
import com.github.klee0kai.test.mowgli.world.History;
import com.github.klee0kai.test.mowgli.identity.Conscience;
import com.github.klee0kai.test.mowgli.identity.Knowledge;

import javax.inject.Inject;
import javax.inject.Provider;
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
    public IRef<Knowledge> knowledgePhantomProvide2;

    @Inject
    public Provider<Knowledge> knowledgePhantomProvide3;

    @Inject
    public PhantomProvide<Knowledge> knowledgePhantomProvide;

    @Override
    public void born() {
        DI.inject(this);
    }
}