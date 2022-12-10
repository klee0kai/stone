package com.github.klee0kai.test.gc.di;

import com.github.klee0kai.stone.annotations.component.*;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.test.cache.di.CacheDataModule;
import com.github.klee0kai.test.gc.di.ann.AppGcScope;
import com.github.klee0kai.test.gc.di.ann.ContextGcScope;


@Component
public interface GComponent extends IComponent {

    AppModule app();

    CacheDataModule data();


    @GcAllScope
    void gcAll();

    @GcWeakScope
    void gcWeak();

    @GcSoftScope
    void gcSoft();

    @GcStrongScope
    void gcStrong();

    @AppGcScope
    void gcApp();

    @ContextGcScope
    void gcContext();

    @AppGcScope
    @ContextGcScope
    void gcAppAndContext();
}
