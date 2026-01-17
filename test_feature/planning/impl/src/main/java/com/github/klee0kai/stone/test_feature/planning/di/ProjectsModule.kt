package com.github.klee0kai.stone.test_feature.planning.di

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.stone.test_feature.planning.project.BuildFactoryProject
import com.github.klee0kai.stone.test_feature.planning.project.BuildFactoryProjectImpl
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProject
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProjectImpl
import com.github.klee0kai.stone.test_feature.planning.store.ProjectsStore

@Module
abstract class ProjectsModule {
    @Provide(cache = Provide.CacheType.Strong)
    abstract fun projectsStore(): ProjectsStore?

    @Provide(cache = Provide.CacheType.Soft)
    open fun logisticProject(): LogisticProject {
        return LogisticProjectImpl()
    }

    @Provide(cache = Provide.CacheType.Soft)
    open fun buildFactoryProject(): BuildFactoryProject {
        return BuildFactoryProjectImpl()
    }
}
