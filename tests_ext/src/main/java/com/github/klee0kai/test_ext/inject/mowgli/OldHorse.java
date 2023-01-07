package com.github.klee0kai.test_ext.inject.mowgli;

import com.github.klee0kai.test.mowgli.animal.Horse;
import com.github.klee0kai.test.mowgli.identity.Ideology;
import com.github.klee0kai.test_ext.inject.OldForest;
import com.github.klee0kai.test_ext.inject.mowgli.diseases.Osteoarthritis;
import com.github.klee0kai.test_ext.inject.mowgli.identity.OldKnowledge;

import javax.inject.Inject;

public class OldHorse extends Horse {

    @Inject
    public OldKnowledge oldKnowledge;

    @Inject
    public Osteoarthritis osteoarthritis;

    @Inject
    public Ideology ideology;

    @Override
    public void born() {
        OldForest.DIPro.inject(this, listener -> {

        });
    }
}
