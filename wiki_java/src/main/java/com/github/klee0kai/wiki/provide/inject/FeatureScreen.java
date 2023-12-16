package com.github.klee0kai.wiki.provide.inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.wiki.provide.identifiers.FeaturePresenter;

import javax.inject.Inject;

public class FeatureScreen {

    @Inject
    public FeaturePresenter presenter;

    @Inject
    void init(FeaturePresenter presenter) {

    }

    void start() {
        AppComponent DI = Stone.createComponent(AppComponent.class);
        DI.inject(this);
    }

}
