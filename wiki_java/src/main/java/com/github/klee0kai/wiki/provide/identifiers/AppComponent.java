package com.github.klee0kai.wiki.provide.identifiers;

import com.github.klee0kai.stone.annotations.component.Component;

@Component(identifiers = {ScreenId.class, LoginId.class})
public interface AppComponent {

    SevenPlanetModule planetsModule();

    ThreadsModule threadsModule();

    PresentersModule presentersModule();

    FeaturePresenter featurePresenter(LoginId loginId, ScreenId screenId);

    void inject(FeatureScreen screen, LoginId loginId, ScreenId screenId);

}
