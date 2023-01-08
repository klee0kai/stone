package com.github.klee0kai.test_ext.inject.mowgli.identity;

import com.github.klee0kai.test.mowgli.identity.Knowledge;

public class OldKnowledge extends Knowledge {


    public boolean doChildKnowledge() {
        return true;
    }

    public boolean isOldKnowledge() {
        return true;
    }

}
