package com.github.klee0kai.test.house.kitchen.storagearea;

import com.github.klee0kai.test.house.qualifiers.StoreAreaType;

public class StoreArea {

    public final StoreAreaType type;
    public final Cookware cookware;
    public final Clothes clothes;
    public final Sanitizers sanitizers;


    public StoreArea(StoreAreaType type,Cookware cookware, Clothes clothes, Sanitizers sanitizers) {
        this.type = type;
        this.cookware = cookware;
        this.clothes = clothes;
        this.sanitizers = sanitizers;
    }
}
