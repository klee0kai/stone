package com.github.klee0kai.test.data;

import java.util.UUID;

public class StoneRepository {

    public UUID uuid = UUID.randomUUID();
    public String userId = null;

    public StoneRepository() {

    }

    public StoneRepository(String userId) {
        this.userId = userId;
    }


}
