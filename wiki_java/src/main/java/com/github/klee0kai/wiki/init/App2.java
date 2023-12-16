package com.github.klee0kai.wiki.init;

import com.github.klee0kai.stone.Stone;

public class App2 {

    public static void main(String[] args) {
        AppComponent DI = Stone.createComponent(AppComponent.class);
        AppProComponent DIPro = Stone.createComponent(AppProComponent.class);
        DIPro.extendComponent(DI);
    }

}