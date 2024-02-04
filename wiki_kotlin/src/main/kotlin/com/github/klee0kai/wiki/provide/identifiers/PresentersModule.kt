package com.github.klee0kai.wiki.provide.identifiers

import com.github.klee0kai.stone.annotations.module.Module
import java.util.concurrent.ThreadPoolExecutor

@Module
abstract class PresentersModule {

    @MyQualifier
    abstract fun provideFeaturePresenter(
        @ThreadQualifier(type = ThreadQualifier.ThreadType.Main) executor: ThreadPoolExecutor
    ): FeaturePresenter

    abstract fun provideFeaturePresenter(
        screenId: ScreenId,
        loginId: LoginId
    ): FeaturePresenter

}
