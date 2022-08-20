package org.example;

import org.example.di.*;

public class SimpleAppExt extends SimpleApp {

    public static final AppComponent DIExt = new AppComponentExtStone();

    @Override
    public void init() {
        super.init();
        DIExt.init(new DomainExtModule(), new PresenterExtModule());
        DIExt.relateTo(DI);
    }

    @Override
    public void start() {
        super.start();


    }
}
