package org.example;

import com.github.klee0kai.stone.container.ItemsWeakContainer;
import org.example.module.InfinityStone;

import static org.example.DynamicFeatureLib.DIExt;
import static org.example.SimpleApp.DI;

public class Main {
    public static void main(String[] args) {

        System.out.println("SimpleAppExt example start ");
        SimpleApp.init();

        System.out.println("start before use dynamic feature");
        SimpleApp.doSomeWork();
        System.out.println("robotRep ver = " + DI.domain().robotRepository().getVer());
        System.out.println("init dynamic feature");
        DynamicFeatureLib.init();
        System.out.println("some simple modules still available");
        System.out.println("robotRep ver = " + DI.domain().robotRepository().getVer());
        System.out.println("need run GarbageCollector");
        ItemsWeakContainer.gc(true);
        System.out.println("After GC. robotRep ver = " + DI.domain().robotRepository().getVer());
        System.out.println("After GC. Reality stone owner is " + DI.domain().stoneRepository(InfinityStone.Reality).getOwner());
        System.out.println("After GC. Mind stone owner is " + DI.domain().stoneRepository(InfinityStone.Mind).getOwner());
        DIExt.domain().stoneRepository(InfinityStone.Reality).tryTake("Tony Stark");
        DIExt.domain().stoneRepository(InfinityStone.Mind).tryTake("Tony Stark");

        System.out.println("SimpleAppExt start before use dynamic feature");
        SimpleApp.doSomeWork();

        DynamicFeatureLib.doSomeWork();

    }
}