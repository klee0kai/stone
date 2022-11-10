package com.github.klee0kai.test.gc.di;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.interfaces.IComponent;
import com.github.klee0kai.test.gc.di.ann.AppGcScope;
import com.github.klee0kai.test.gc.di.ann.ContextGcScope;


@Component
public interface GComponent extends IComponent {

    AppModule app();


    @GcAllScope
    void gcAll();

    @AppGcScope
    void gcApp();

    @ContextGcScope
    void gcContext();

    @AppGcScope
    @ContextGcScope
    void gcAppAndContext();
}
