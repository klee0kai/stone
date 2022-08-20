package org.example;

import com.klee0kai.stone.container.ItemsWeakContainer;

public class Main {
    public static void main(String[] args) {

        System.out.println("SimpleAppExt example start ");
        SimpleApp.init();

        System.out.println("start before use dynamic feature");
        SimpleApp.doSomeWork();
        System.out.println("robotRep ver = " + SimpleApp.DI.domain().robotRepository().getVer());
        System.out.println("init dynamic feature");
        DynamicFeatureLib.init();
        System.out.println("some simple modules still available");
        System.out.println("robotRep ver = " + SimpleApp.DI.domain().robotRepository().getVer());
        System.out.println("need run GarbageCollector");
        ItemsWeakContainer.gc(true);
        System.out.println("After GC. robotRep ver = " + SimpleApp.DI.domain().robotRepository().getVer());
        System.out.println("SimpleAppExt start before use dynamic feature");
        SimpleApp.doSomeWork();

        DynamicFeatureLib.doSomeWork();

    }
}