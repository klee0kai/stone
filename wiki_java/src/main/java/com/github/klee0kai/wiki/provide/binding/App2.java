package com.github.klee0kai.wiki.provide.binding;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

public class App2 {

    public static void main(String[] args) {
        SunSystemComponent DI = Stone.createComponent(SunSystemComponent.class);
        Sun sun = new Sun();
        DI.sun(sun);


        Sun sunFromDI = DI.sun(null);
        System.out.println("sunFromDI " + sunFromDI);
    }

}
