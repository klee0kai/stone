package org.example;

import com.klee0kai.stone.Stone;
import com.klee0kai.stone.container.ItemsWeakContainer;
import org.example.di.AppComponent;
import org.example.di.DomainModule;
import org.example.di.PresenterModule;
import org.example.module.InfinityStone;

public class SimpleApp {

    public static final AppComponent DI = Stone.createComponent(AppComponent.class);

    public static void init() {
        DI.init(new DomainModule(), new PresenterModule());
    }


    public static void doSomeWork() {
        System.out.println("user presenter1 = " + DI.presenter().userPresenter());
        System.out.println("user presenter2 = " + DI.presenter().userPresenter());
        ItemsWeakContainer.gc(false);
        System.out.println("After GC. user presenter = " + DI.presenter().userPresenter());
        System.out.println("user user presenter  = " + DI.presenter().userPresenter());

        DI.domain().stoneRepository(InfinityStone.Reality).tryTake("Thanos");
        DI.domain().stoneRepository(InfinityStone.Mind).tryTake("Vision");
        System.out.println("Reality stone owner is " + DI.domain().stoneRepository(InfinityStone.Reality).getOwner());
        System.out.println("Mind stone owner is " + DI.domain().stoneRepository(InfinityStone.Mind).getOwner());

    }

}
