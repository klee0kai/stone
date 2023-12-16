package com.github.klee0kai.wiki.start;


import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.mowgli.galaxy.Earth;

class App {

    static SevenPlanetComponent DI = Stone.createComponent(SevenPlanetComponent.class);

    public static void main(String[] args) {
        Earth earth = DI.planets().earth();
    }

}
