package com.github.klee0kai.test.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.stone.annotations.component.Inject;
import com.github.klee0kai.test.data.StoneRepository;
import com.github.klee0kai.test.net.StoneApi;
import com.github.klee0kai.test.qualifiers.di.QTestComponent;
import com.github.klee0kai.test.qualifiers.di.qualifiers.ProductType;
import com.github.klee0kai.test.qualifiers.di.qualifiers.Token;
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId;

public class QApp {

    public QTestComponent DI = Stone.createComponent(QTestComponent.class);

    @Inject
    public StoneRepository stoneRepository;
    @Inject
    public StoneApi stoneApi;

    public void startSimple() {
        DI.inject(this);
    }

    public void startDebug1() {
        DI.inject(this, ProductType.DEBUG);
    }

    public void startDemo1() {
        //check independence to sequence
        DI.inject(ProductType.DEMO, new UserId("demo_user_id"), new Token("demo_token"), this);
    }

    public void startRelease() {
        DI.inject(this, new Token("release_token"), ProductType.RELEASE);
    }

}
