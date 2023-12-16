package com.github.klee0kai.wiki.provide.binding;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.mowgli.galaxy.Sun;

public class App {

    public static void main(String[] args) {
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();
        DI.sunSystem().sun(sun);

        Sun sunFromDI = DI.sunSystem().sun(null);
        System.out.println("sunFromDI " + sunFromDI);
    }

}
