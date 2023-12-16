package com.github.klee0kai.wiki.init;

import com.github.klee0kai.stone.Stone;

public class App {

    public static void main(String[] args) {
        // init stage
        FeatureModule module = new FeatureModule();
        AppComponent DI = Stone.createComponent(AppComponent.class);
        DI.initFeatureModule(module);
        // some work
        // dynamic feature loaded
        DynamicFeatureModule moduleNewFeatures = new DynamicFeatureModule();
        DI.initFeatureModule(moduleNewFeatures);
    }

}
