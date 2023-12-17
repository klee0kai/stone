package com.github.klee0kai.wiki.start;


import com.github.klee0kai.test.mowgli.galaxy.Earth;

class App2 {

    static SevenPlanetComponent DI = new SevenPlanetComponentStoneComponent();

    public static void main(String[] args) {
        Earth earth = DI.planets().earth();
    }

}
