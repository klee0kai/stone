package com.github.klee0kai.test.mowgli.earth;

import java.util.UUID;


public class Cave implements IMountain {

    public UUID uuid = UUID.randomUUID();

    public Cave() {
        //nothing
    }

    public Cave(CaveType type, int deep) {
        //nothing
    }

    public  enum CaveType {
        Solutional, Glacier, Fracture
    }

}
