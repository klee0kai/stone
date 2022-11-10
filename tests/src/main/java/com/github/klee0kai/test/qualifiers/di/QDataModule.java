package com.github.klee0kai.test.qualifiers.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.data.StoneRepository;
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId;


@Module
public abstract class QDataModule {

    public StoneRepository stoneRepository() {
        return new StoneRepository();
    }


    public StoneRepository stoneRepository(UserId userId) {
        return new StoneRepository(userId.userId);
    }


}
