package com.github.klee0kai.test_ext.inject;

import com.github.klee0kai.test.inject.Horse;
import com.github.klee0kai.test.inject.identity.Ideology;
import com.github.klee0kai.test_ext.inject.di.OldIdentityModule;
import com.github.klee0kai.test_ext.inject.diseases.Osteoarthritis;
import com.github.klee0kai.test_ext.inject.identity.OldKnowledge;

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
        OldForest.DIPro.inject(this);
    }
}
