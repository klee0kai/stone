package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.annotations.Provide;
import com.github.klee0kai.test.data.StoneRepository;


@Module
public abstract class QDataModule {

    public StoneRepository stoneRepository() {
        return new StoneRepository();
    }


    public StoneRepository stoneRepository(String userId) {
        return new StoneRepository(userId);
    }


}
