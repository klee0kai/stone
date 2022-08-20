package org.example.domain;

import org.example.module.InfinityStone;

public class StoneRepository {

    private String owner;
    protected final InfinityStone infinityStone;

    public StoneRepository(InfinityStone infinityStone) {
        this.infinityStone = infinityStone;
    }

    public void tryTake(String owner) {
        if (this.owner == null)
            this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public InfinityStone getInfinityStone() {
        return infinityStone;
    }
}
