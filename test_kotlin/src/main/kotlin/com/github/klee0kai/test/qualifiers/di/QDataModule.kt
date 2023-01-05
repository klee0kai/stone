package com.github.klee0kai.test.qualifiers.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.test.data.StoneRepository
import com.github.klee0kai.test.qualifiers.di.qualifiers.UserId

@Module
abstract class QDataModule {
    open fun stoneRepository(): StoneRepository {
        return StoneRepository()
    }

    open fun stoneRepository(userId: UserId): StoneRepository {
        return StoneRepository(userId.userId)
    }
}