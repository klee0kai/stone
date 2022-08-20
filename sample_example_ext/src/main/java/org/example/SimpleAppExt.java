package org.example;

import org.example.di.*;

public class SimpleAppExt extends SimpleApp {

    public static final AppComponent DIExt = new AppComponentExtStone();

    @Override
    public void init() {
        super.init();
        DIExt.init(new DomainExtModule(), new PresenterExtModule());
        DIExt.relateTo(DI);
        System.out.println("SimpleAppExt.init DI.prefix = " + DI.prefix());
        System.out.println("SimpleAppExt.init DIExt.prefix = " + DIExt.prefix());
    }

    @Override
    public void start() {
        super.start();


    }
}
