package org.example;

import org.example.di.*;

import static org.example.SimpleApp.DI;

public class DynamicFeatureLib {

    public static final AppComponentExt DIExt = new AppComponentExtStone();


    public static void init() {
        DIExt.init(new DomainExtModule(), new PresenterExtModule());
        DI.init(DIExt);
    }

    public static void doSomeWork() {
        System.out.println("is ext method available " + DIExt.domain().robotRepository().isExt());
    }

}
