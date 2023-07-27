package com.github.klee0kai.stone.test_feature.planning.di;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.test_feature.planning.project.BuildFactoryProject;
import com.github.klee0kai.stone.test_feature.planning.project.BuildFactoryProjectImpl;
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProject;
import com.github.klee0kai.stone.test_feature.planning.project.LogisticProjectImpl;
import com.github.klee0kai.stone.test_feature.planning.store.ProjectsStore;

@Module
public abstract class ProjectsModule {

    @Provide(cache = Provide.CacheType.Strong)
    public abstract ProjectsStore projectsStore();

    @Provide(cache = Provide.CacheType.Soft)
    public LogisticProject logisticProject() {
        return new LogisticProjectImpl();
    }

    @Provide(cache = Provide.CacheType.Soft)
    public BuildFactoryProject buildFactoryProject() {
        return new BuildFactoryProjectImpl();
    }


}
