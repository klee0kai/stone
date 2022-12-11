package com.github.klee0kai.test_ext.inject.identity;

import com.github.klee0kai.test.inject.identity.Ideology;

import java.util.UUID;

public class FamilyIdeology extends Ideology {

    public UUID uuid = UUID.randomUUID();


    public boolean isFamilyIdeology() {
        return true;
    }

}
