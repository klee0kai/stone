package com.github.klee0kai.wiki.provide.identifiers;

import com.github.klee0kai.stone.annotations.module.Module;

import java.util.concurrent.ThreadPoolExecutor;

@Module
public abstract class PresentersModule {

    @MyQualifier
    public abstract FeaturePresenter provideFeaturePresenter(
            @ThreadQualifier(type = ThreadQualifier.ThreadType.Main) ThreadPoolExecutor executor
    );

    public abstract FeaturePresenter provideFeaturePresenter(
            ScreenId screenId,
            LoginId loginId
    );

}
