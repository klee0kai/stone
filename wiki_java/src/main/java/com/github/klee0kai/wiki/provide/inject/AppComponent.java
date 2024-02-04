package com.github.klee0kai.wiki.provide.inject;

import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner;
import com.github.klee0kai.wiki.provide.identifiers.LoginId;
import com.github.klee0kai.wiki.provide.identifiers.PresentersModule;
import com.github.klee0kai.wiki.provide.identifiers.ScreenId;
import com.github.klee0kai.wiki.provide.identifiers.ThreadsModule;

@Component(identifiers = {ScreenId.class, LoginId.class})
public interface AppComponent {

    ThreadsModule threadsModule();

    PresentersModule presentersModule();

    void inject(FeatureScreen screen);

    void inject(FeatureScreen screen, StoneLifeCycleOwner owner, LoginId loginId, ScreenId screenId);

}
