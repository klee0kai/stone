package org.example;

import com.klee0kai.stone.container.ItemsWeakContainer;
import org.example.di.AppComponent;
import org.example.di.AppComponentStone;
import org.example.di.DomainModule;
import org.example.di.PresenterModule;

public class SimpleApp {

    public static final AppComponent DI = new AppComponentStone();

    public static void init() {
        DI.init(new DomainModule(), new PresenterModule());
    }


    public static void doSomeWork() {
        System.out.println("user rep1 = " + DI.domain().userRepository());
        System.out.println("user rep2 = " + DI.domain().userRepository());
        System.out.println("user rep3 = " + DI.domain().userRepository());
        ItemsWeakContainer.gc(true);
        System.out.println("user rep after gc = " + DI.domain().userRepository());
        System.out.println("user rep2 = " + DI.domain().userRepository());
    }

}
